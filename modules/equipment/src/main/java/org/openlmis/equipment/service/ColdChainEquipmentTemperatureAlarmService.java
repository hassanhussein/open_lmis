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

import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.helper.CommaSeparator;
import org.openlmis.equipment.domain.ColdChainEquipmentTemperatureAlarm;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.dto.ColdChainTemperatureAlarmDTO;
import org.openlmis.equipment.dto.ColdTraceAlarmDTO;
import org.openlmis.equipment.repository.ColdChainEquipmentTemperatureAlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ColdChainEquipmentTemperatureAlarmService {

  @Autowired
  private ColdChainEquipmentTemperatureAlarmRepository repository;

  @Autowired
  private EquipmentInventoryService inventoryService;

  @Autowired
  private CommaSeparator commaSeparator;

  public void save(ColdChainTemperatureAlarmDTO alarmDto, Long userId) {
    // check if the alarmDto is already recorded

    ColdChainEquipmentTemperatureAlarm persistedAlarm = repository.getAlarmById(alarmDto.getAlert_id());
    if (persistedAlarm != null) {
      // this is an update
      persistedAlarm.setModifiedBy(userId);

      persistedAlarm.setModifiedDate(new Date());
      persistedAlarm.setAlarmType(alarmDto.getAlert_type());
      persistedAlarm.setStartTime(alarmDto.getStart_ts());
      persistedAlarm.setEndTime(alarmDto.getEnd_ts());
      persistedAlarm.setStatus(alarmDto.getStatus().getValue());

      repository.update(persistedAlarm);
    } else {
      // save new.
      EquipmentInventory inventory = inventoryService.getInventoryBySerialNumber(alarmDto.getDevice_id());
      if (inventory == null) {
        throw new DataException("Equipment not found. Please check the serial number you provided.");
      }

      ColdChainEquipmentTemperatureAlarm toPersist = ColdChainEquipmentTemperatureAlarm
          .builder()
          .equipmentInventoryId(inventory.getId())
          .alarmId(alarmDto.getAlert_id())
          .alarmType(alarmDto.getAlert_type())
          .startTime(alarmDto.getStart_ts())
          .endTime(alarmDto.getEnd_ts())
          .status(alarmDto.getStatus().getValue())
          .build();
      toPersist.setCreatedBy(userId);
      toPersist.setCreatedDate(new Date());
      repository.insert(toPersist);
    }

  }


  public List<ColdChainEquipmentTemperatureAlarm> getAllAlarms(String serialNumber) {
    EquipmentInventory inventory = inventoryService.getInventoryBySerialNumber(serialNumber);
    return repository.getAllAlarmsByEquipementInventory(inventory.getId());
  }

  public List<ColdChainEquipmentTemperatureAlarm> getAlarmsForPeriod(String serialNumber, Long periodId) {
    EquipmentInventory inventory = inventoryService.getInventoryBySerialNumber(serialNumber);
    return repository.getAlarmsByEquipmentInventoryPeriod(inventory.getId(), periodId);
  }

  public List<ColdTraceAlarmDTO> getAlarmsForFacilityPeriod(Long facilityId, Long programId, Long periodId) {
    List<EquipmentInventory> inventories = inventoryService.getInventoryByFacilityAndProgram(facilityId, programId);
    return repository.getAlarmsByEquipmentInventoriesAndPeriod(commaSeparator.commaSeparateIds(inventories), periodId);
  }
}
