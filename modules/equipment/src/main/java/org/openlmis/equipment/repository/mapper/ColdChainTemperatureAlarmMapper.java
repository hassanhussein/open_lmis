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

package org.openlmis.equipment.repository.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.equipment.domain.ColdChainEquipmentTemperatureAlarm;
import org.openlmis.equipment.dto.ColdTraceAlarmDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColdChainTemperatureAlarmMapper {


  @Insert("insert into cold_chain_equipment_temperature_alarms " +
      "(equipmentInventoryId, alarmId, startTime, endTime, alarmType, status , createdBy, createdDate)" +
      "values" +
      "(#{equipmentInventoryId}, #{alarmId}, #{startTime}, #{endTime}, #{alarmType}, #{status}, #{createdBy}, #{createdDate})")
  void insert(ColdChainEquipmentTemperatureAlarm alarm);

  @Update("update cold_chain_equipment_temperature_alarms set " +
      "equipmentInventoryId = #{equipmentInventoryId}, " +
      "alarmId = #{alarmId}, " +
      "startTime = #{startTime}, " +
      "endTime = #{endTime}, " +
      "alarmType = #{alarmType}, " +
      "status = #{status}, " +
      "createdBy = #{modifiedBy}, " +
      "modifiedDate = NOW() " +
      "where id = #{id} ")
  void update(ColdChainEquipmentTemperatureAlarm alarm);

  @Select("select * from cold_chain_equipment_temperature_alarms where alarmId = #{alarmId}")
  ColdChainEquipmentTemperatureAlarm getAlarmByAlarmId(@Param("alarmId") String alarmId);

  @Select("select * from cold_chain_equipment_temperature_alarms " +
      "where equipmentInventoryId = #{equipmentInventoryId} " +
      "and startTime >= (select p.startDate from processing_periods p where p.id = #{period}) " +
      "and startTime <= (select p.endDate from processing_periods p where p.id = #{period})")
  List<ColdChainEquipmentTemperatureAlarm> getAlarmByEquipmentInventoryAndPeriod(@Param("equipmentInventoryId") Long equipmentInventoryId, @Param("period") Long periodId);

  @Select("select * from cold_chain_equipment_temperature_alarms " +
      "where equipmentInventoryId = #{equipmentInventoryId} ")
  List<ColdChainEquipmentTemperatureAlarm> getAlarmByEquipmentInventory(@Param("equipmentInventoryId") Long equipmentInventoryId);

  @Select("select ca.*, ei.serialNumber, e.model, e.name as equipmentName from cold_chain_equipment_temperature_alarms ca " +
      " JOIN equipment_inventories ei on ei.id = ca.equipmentInventoryId " +
      " JOIN equipments e on e.id = ei.equipmentId " +
      "   where  " +
      " ca.startTime >= (select p.startDate from processing_periods p where p.id = #{period}) " +
      " and ca.startTime <= (select p.endDate from processing_periods p where p.id = #{period})" +
      " and ca.equipmentInventoryId = ANY(#{equipmentInventoryIds}::INT[])")
  List<ColdTraceAlarmDTO> getAlarmByEquipmentInventoriesAndPeriod(@Param("equipmentInventoryIds") String equipmentInventoryIds, @Param("period") Long period);

}
