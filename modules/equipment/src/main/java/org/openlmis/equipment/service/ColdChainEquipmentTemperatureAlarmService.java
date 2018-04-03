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
import org.openlmis.equipment.domain.ColdChainEquipmentTemperatureAlarm;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.dto.ColdChainEquipmentTemperatureAlarmDTO;
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

  public void save(ColdChainEquipmentTemperatureAlarmDTO alarmDto, Long userId) {
    // check if the alarmDto is already recorded

    ColdChainEquipmentTemperatureAlarm persistedAlarm = repository.getAlarmById(alarmDto.getAlarmId());
    if (persistedAlarm != null) {
      // this is an update
      persistedAlarm.setModifiedBy(userId);

      persistedAlarm.setModifiedDate(new Date());
      persistedAlarm.setAlarmDate(alarmDto.getAlarmDate());
      persistedAlarm.setAlarmType(alarmDto.getAlarmType());
      persistedAlarm.setStartTime(alarmDto.getStartTime());
      persistedAlarm.setEndTime(alarmDto.getEndTime());
      persistedAlarm.setStatus(alarmDto.getStatus());

      repository.update(persistedAlarm);
    } else {
      // save new.
      EquipmentInventory inventory = inventoryService.getInventoryBySerialNumber(alarmDto.getSerialNumber());
      if (inventory == null) {
        throw new DataException("Equipment not found. Please check the serial number you provided.");
      }

      ColdChainEquipmentTemperatureAlarm toPersist = ColdChainEquipmentTemperatureAlarm
          .builder()
          .equipmentInventoryId(inventory.getId())
          .alarmDate(alarmDto.getAlarmDate())
          .alarmId(alarmDto.getAlarmId())
          .alarmType(alarmDto.getAlarmType())
          .startTime(alarmDto.getStartTime())
          .endTime(alarmDto.getEndTime())
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
    return repository.getAlarmsByEquipementInventoryPeriod(inventory.getId(), periodId);
  }
}
