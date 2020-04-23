/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.equipment.service;

import org.apache.log4j.Logger;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.service.FacilityService;
import org.openlmis.equipment.domain.*;
import org.openlmis.equipment.dto.ColdChainEquipmentTemperatureStatusDTO;
import org.openlmis.equipment.dto.EquipmentChangeLogDto;
import org.openlmis.equipment.repository.EquipmentInventoryRepository;
import org.openlmis.equipment.repository.EquipmentRepository;
import org.openlmis.equipment.repository.mapper.EquipmentInventoryChangeLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.openlmis.core.domain.RightName.MANAGE_EQUIPMENT_INVENTORY;

@Component
public class EquipmentInventoryService {

  @Autowired
  EquipmentInventoryRepository repository;
  Logger logger = Logger.getLogger(EquipmentInventoryService.class);
  @Autowired
  private FacilityService facilityService;
  @Autowired
  private EquipmentService equipmentService;
  @Autowired
  private EquipmentRepository equipmentRepository;
  @Autowired
  private EquipmentInventoryChangeLogMapper changeLogMapper;


  public List<EquipmentInventory> getInventoryForFacility(Long facilityId, Long programId){
    return repository.getFacilityInventory(facilityId, programId);
  }

  public List<EquipmentInventory> getInventory(Long userId, Long typeId, Long programId, Long equipmentTypeId, Pagination pagination) {
    long[] facilityIds = getFacilityIds(userId, typeId, programId);

    return repository.getInventory(programId, equipmentTypeId, facilityIds, pagination);
  }

  public Integer getInventoryCount(Long userId, Long typeId, Long programId, Long equipmentTypeId) {
    long[] facilityIds = getFacilityIds(userId, typeId, programId);

    return repository.getInventoryCount(programId, equipmentTypeId, facilityIds);
  }

  private long[] getFacilityIds(Long userId, Long typeId, Long programId) {
    // Get list of facilities
    List<Facility> facilities;
    if (typeId == 0) {
      facilities = Arrays.asList(facilityService.getHomeFacility(userId));
    } else {
      facilities = facilityService.getUserSupervisedFacilities(userId, programId, MANAGE_EQUIPMENT_INVENTORY);
    }

    // From facilities, get facility ids
    long[] facilityIds = new long[facilities.size()];
    int index = 0;
    for (Facility f : facilities) {
      facilityIds[index++] = f.getId();
    }

    return facilityIds;
  }

  public EquipmentInventory getInventoryById(Long id){
    return repository.getInventoryById(id);
  }

  public void save(EquipmentInventory inventory){
    // First, may need to save equipment into equipment list
    // Only need to do this for non-CCE
    Equipment equipment = inventory.getEquipment();
    if (!equipment.getEquipmentType().isColdChain()) {
      //Boolean equipmentFound = false;
      Long equipmentTypeId = equipment.getEquipmentTypeId();

      // Check to see if equipment already exists in db
     // List<Equipment> equipments = equipmentService.getAllByType(equipmentTypeId);

      Equipment existingEquipment = equipmentService.getByTypeManufacturerAndModel(equipmentTypeId,
              equipment.getManufacturer(),
              equipment.getEquipmentModel().getId(),
              equipment.getModel());

      //equipment = existingEquipment == null ? equipment : existingEquipment;

      equipment.setModifiedBy(inventory.getModifiedBy());
      equipment.setModel(null);
      if (existingEquipment == null) {
        equipment.setCreatedBy(inventory.getCreatedBy());
        equipmentRepository.insert(equipment);
      } else {
        equipmentRepository.update(equipment);
      }

      // Make sure equipmentId is set for the inventory save, equipment.id is filled in after insert
      inventory.setEquipmentId(equipment.getId());
    }
    EquipmentInventoryChangeLog changeLog = null;
    if (inventory.getId() == null) {

      repository.insert(inventory);
      changeLog = EquipmentInventoryChangeLog.builder()
          .changeType("ADD")
          .fieldName("ALL")
          .newValue(null)
          .equipmentInventoryId(inventory.getId())
          .build();
    } else {
      changeLog = getChangeLog(inventory);
      repository.update(inventory);
    }
    if (changeLog != null) {
      changeLog.setCreatedBy(inventory.getCreatedBy());
      changeLog.setCreatedDate(new Date());
      changeLogMapper.insert(changeLog);
    }
  }

  private EquipmentInventoryChangeLog getChangeLog(EquipmentInventory inventory) {
    EquipmentInventory persistedInventory = repository.getInventoryById(inventory.getId());

    if (!Objects.equals(persistedInventory.getSerialNumber(), inventory.getSerialNumber())) {
      // this is a changed serial number.
      return EquipmentInventoryChangeLog.builder()
          .changeType("CHANGE_SERIAL_NUMBER")
          .equipmentInventoryId(inventory.getId())
          .fieldName("SERIAL_NUMBER")
          .newValue(inventory.getSerialNumber())
          .previousValue(persistedInventory.getSerialNumber())
          .build();
    } else if (persistedInventory.getIsActive() && !inventory.getIsActive()) {
      // This is deleted or deactivated.
      return EquipmentInventoryChangeLog.builder()
          .changeType("DEACTIVATE")
          .equipmentInventoryId(inventory.getId())
          .previousValue("true")
          .newValue("false")
          .fieldName("IS_ACTIVE")
          .previousValue(persistedInventory.getIsActive().toString())
          .build();
    } else if (!persistedInventory.getIsActive() && inventory.getIsActive()) {
      return EquipmentInventoryChangeLog.builder()
          .changeType("ACTIVATE")
          .previousValue("false")
          .newValue("true")
          .fieldName("IS_ACTIVE")
          .equipmentInventoryId(inventory.getId())
          .previousValue(persistedInventory.getIsActive().toString())
          .build();
    }
    return null;
  }

  public List<EquipmentChangeLogDto> getEquipmentLogDtos(Date date) {
    return changeLogMapper.getChangeLogsAfterDate(date);
  }

  public void updateStatus(EquipmentInventory inventory){
    repository.updateStatus(inventory);
  }

  public void updateNonFunctionalEquipments() {
    repository.updateNonFunctionalEquipments();
  }

public List<ColdChainEquipmentTemperatureStatusDTO>getAllbyId(Long equipmentId){
    return repository.getAll(equipmentId);
  }

  public void deleteEquipmentInventory(Long inventoryId){
    repository.deleteEquipmentInventory(inventoryId);
  }


  public List<EquipmentInventory>getInventoryByFacilityAndProgram(Long facilityId, Long programId){
    return repository.getInventoryByFacilityAndProgram(facilityId,programId);
  }

  public List<NonFunctionalTestTypes> getBioChemistryEquipmentTestTypes(){
    return equipmentService.getBioChemistryEquipmentTestTypes();
  }

  public List<ManualTestTypes> getManualTestTypes(){
    return equipmentService.getManualTestTypes();
  }

  public EquipmentInventory getInventoryBySerialNumber(String serialNumber) {
    return repository.findBySerialNumber(serialNumber);
  }

    public Integer getInventoryCountBySearch(String searchParam, Long userId, Long typeId, Long programId, Long equipmentTypeId) {

      long[] facilityIds = getFacilityIds(userId, typeId, programId);
      Integer count = repository.getInventoryCountBySearch(searchParam,programId, equipmentTypeId, facilityIds);
      System.out.println(count);

      logger.info(count);
      return count;
    }

  public List<EquipmentInventory> searchInventory(String searchParam, Long userId, Long typeId, Long programId, Long equipmentTypeId,Pagination pagination) {
    long[] facilityIds = getFacilityIds(userId, typeId, programId);

    return repository.searchInventory(searchParam,programId, equipmentTypeId, facilityIds,pagination);
  }
}
