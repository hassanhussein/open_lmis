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
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.Program;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.ProgramService;
import org.openlmis.equipment.domain.*;
import org.openlmis.equipment.dto.ColdChainEquipmentTemperatureStatusDTO;
import org.openlmis.equipment.dto.EquipmentInventoryUploadDto;
import org.openlmis.equipment.repository.EquipmentInventoryRepository;
import org.openlmis.equipment.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.openlmis.core.domain.RightName.MANAGE_EQUIPMENT_INVENTORY;

@Component
public class EquipmentInventoryService {

    @Autowired
    EquipmentInventoryRepository repository;

    Logger logger = Logger.getLogger(EquipmentInventoryService.class);

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    EquipmentOperationalStatusService equipmentOperationalStatusService;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    ProgramService programService;

    public List<EquipmentInventory> getInventoryForFacility(Long facilityId, Long programId) {
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

    public EquipmentInventory getInventoryById(Long id) {
        return repository.getInventoryById(id);
    }

    public void save(EquipmentInventory inventory) {
        // First, may need to save equipment into equipment list
        // Only need to do this for non-CCE
        Equipment equipment = inventory.getEquipment();
        if (!equipment.getEquipmentType().getIsColdChain()) {
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

        if (inventory.getId() == null) {
            repository.insert(inventory);
        } else {
            repository.update(inventory);
        }
    }

    public void updateStatus(EquipmentInventory inventory) {
        repository.updateStatus(inventory);
    }

    public void updateNonFunctionalEquipments() {
        repository.updateNonFunctionalEquipments();
    }

    public List<ColdChainEquipmentTemperatureStatusDTO> getAllbyId(Long equipmentId) {
        return repository.getAll(equipmentId);
    }

    public void deleteEquipmentInventory(Long inventoryId) {
        repository.deleteEquipmentInventory(inventoryId);
    }


    public List<EquipmentInventory> getInventoryByFacilityAndProgram(Long facilityId, Long programId) {
        return repository.getInventoryByFacilityAndProgram(facilityId, programId);
    }

    public List<NonFunctionalTestTypes> getBioChemistryEquipmentTestTypes() {
        return equipmentService.getBioChemistryEquipmentTestTypes();
    }

    public List<ManualTestTypes> getManualTestTypes() {
        return equipmentService.getManualTestTypes();
    }

    public EquipmentInventory getInventoryBySerialNumber(String serialNumber) {
        return repository.findBySerialNumber(serialNumber);
    }

    public EquipmentInventory getExistingInventoryForUpload
            (EquipmentInventory equipmentInventory) {
        EquipmentInventory inventory = loadEquipmentInventoryUploadFields(equipmentInventory);
        return repository.getInventoryByFacilityProgramEquipmentSerialNumber(
                inventory.getFacilityId(),
                inventory.getProgramId(),
                inventory.getEquipmentId(),
                inventory.getSerialNumber());
    }

    public void uploadEquipmentInventory(EquipmentInventory equipmentInventory) {
        EquipmentInventory inventory = loadEquipmentInventoryUploadFields(equipmentInventory);
        validateEquipmentInventoryUploadFields(inventory);

        if (inventory.getId() == null) {
            repository.insert(inventory);
        } else {
            repository.update(inventory);
        }
    }

    private void validateEquipmentInventoryUploadFields
            (EquipmentInventory equipmentInventory) {

        if (equipmentInventory.getOperationalStatusId() == null)
            throw new DataException("equipment.operational.status.missing.data");

        if (equipmentInventory.getEquipmentId() == null)
            throw new DataException("equipment.missing.data");

        if (equipmentInventory.getFacilityId() == null)
            throw new DataException("facility.missing.data");
    }

    private EquipmentInventory loadEquipmentInventoryUploadFields(EquipmentInventory equipmentInventory) {
        EquipmentOperationalStatus status = equipmentOperationalStatusService
                .getByCode(equipmentInventory.getEquipmentOperationalStatus().getCode());

        Equipment equipment = equipmentService.getEquipmentByNameAndModelCode(
                equipmentInventory.getEquipment().getName(),
                equipmentInventory.getEquipmentModel().getCode());
        Program program = null;
        if (equipmentInventory.getProgram() != null && equipmentInventory.getProgram().getCode() != null && !equipmentInventory.getProgram().getCode().trim().equals("")) {
            program = programService.getByCode(equipmentInventory.getProgram().getCode());
        }
        Facility facility = facilityService.getFacilityByCode(equipmentInventory.getFacility().getCode());

    /*equipmentInventory.setProgramId(Optional.ofNullable(program.getId()).orElse(null));
    equipmentInventory.setEquipmentId(Optional.ofNullable(equipment.getId()).orElse(null));
    equipmentInventory.setFacilityId(Optional.ofNullable(facility.getId()).orElse(null));
    equipmentInventory.setOperationalStatusId(Optional.ofNullable(status.getId()).orElse(null));
    */

        Optional.ofNullable(equipment).ifPresent(e -> equipmentInventory.setEquipmentId(e.getId()));
        Optional.ofNullable(program).ifPresent(p -> equipmentInventory.setProgramId(p.getId()));
        Optional.ofNullable(facility).ifPresent(f -> equipmentInventory.setFacilityId(f.getId()));
        Optional.ofNullable(status).ifPresent(s -> equipmentInventory.setOperationalStatusId(s.getId()));

        return equipmentInventory;
    }

    public void addEquipmentStatus(List<EquipmentInventoryStatus> equipmentInventoryStatusList) {

        repository.addEquipmentStatus(equipmentInventoryStatusList);
    }
}
