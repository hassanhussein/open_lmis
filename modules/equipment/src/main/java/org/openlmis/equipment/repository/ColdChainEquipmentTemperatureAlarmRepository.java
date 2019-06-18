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

package org.openlmis.equipment.repository;

import org.openlmis.equipment.domain.ColdChainEquipmentTemperatureAlarm;
import org.openlmis.equipment.dto.ColdTraceAlarmDTO;
import org.openlmis.equipment.repository.mapper.ColdChainTemperatureAlarmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ColdChainEquipmentTemperatureAlarmRepository {

  @Autowired
  ColdChainTemperatureAlarmMapper alarmMapper;

  public void insert(ColdChainEquipmentTemperatureAlarm alarm) {
    alarmMapper.insert(alarm);
  }

  public void update(ColdChainEquipmentTemperatureAlarm alarm) {
    alarmMapper.update(alarm);
  }

  public ColdChainEquipmentTemperatureAlarm getAlarmById(String id) {
    return alarmMapper.getAlarmByAlarmId(id);
  }

  public List<ColdChainEquipmentTemperatureAlarm> getAlarmsByEquipmentInventoryPeriod(Long equipmentInventoryId, Long period) {
    return alarmMapper.getAlarmByEquipmentInventoryAndPeriod(equipmentInventoryId, period);
  }

  public List<ColdTraceAlarmDTO> getAlarmsByEquipmentInventoriesAndPeriod(String equipmentInventoryIds, Long period) {
    return alarmMapper.getAlarmByEquipmentInventoriesAndPeriod(equipmentInventoryIds, period);
  }

  public List<ColdChainEquipmentTemperatureAlarm> getAllAlarmsByEquipementInventory(Long equipmentInventoryId) {
    return alarmMapper.getAlarmByEquipmentInventory(equipmentInventoryId);
  }

  public void deleteAlert(String alert_id) {
    alarmMapper.deleteAlert(alert_id);
  }
}