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

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.Facility;
import org.openlmis.equipment.domain.Equipment;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.dto.ColdChainEquipmentTemperatureStatusDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentInventoryMapper {

  @Select("SELECT * from equipment_inventories where facilityId = #{facilityId} and programId = #{programId}")
  @Results({
      @Result(property = "equipmentId", column = "equipmentId"),
      @Result(
          property = "equipment", column = "equipmentId", javaType = Equipment.class,
          one = @One(select = "org.openlmis.equipment.repository.mapper.EquipmentMapper.getById"))
  })
  List<EquipmentInventory> getInventoryByFacilityAndProgram(@Param("facilityId") Long facilityId, @Param("programId")Long programId);

  @Select("SELECT ei.*, (select count(ds.*) > 0 from equipment_daily_cold_trace_status ds where ds.serialnumber = ei.serialnumber) as submittedDailyStatus" +
      " FROM equipment_inventories ei" +
      " JOIN equipments e ON ei.equipmentId = e.id" +
      " JOIN equipment_types et ON e.equipmentTypeId = et.id" +
      " WHERE ei.programId = #{programId}" +
      " AND et.id = #{equipmentTypeId}" +
      " AND ei.facilityId = ANY (#{facilityIds}::INT[])")
  @Results({
      @Result(property = "equipmentId", column = "equipmentId"),
      @Result(
          property = "equipment", column = "equipmentId", javaType = Equipment.class,
          one = @One(select = "org.openlmis.equipment.repository.mapper.EquipmentMapper.getById")),
      @Result(property = "facilityId", column = "facilityId"),
      @Result(
          property = "facility", column = "facilityId", javaType = Facility.class,
          one = @One(select = "org.openlmis.core.repository.mapper.FacilityMapper.getById")),
      @Result(property = "coldChainLineItems", javaType = List.class, column = "equipmentInventoryId",
           many = @Many(select = "getLineItemsByEquipmentInventory"))

  })
  List<EquipmentInventory> getInventory(@Param("programId")Long programId, @Param("equipmentTypeId")Long equipmentTypeId, @Param("facilityIds")String facilityIds, RowBounds rowBounds);

  @Select("SELECT COUNT(ei.id)" +
      " FROM equipment_inventories ei" +
      " JOIN equipments e ON ei.equipmentId = e.id" +
      " JOIN equipment_types et ON e.equipmentTypeId = et.id" +
      " WHERE ei.programId = #{programId}" +
      " AND et.id = #{equipmentTypeId}" +
      " AND ei.facilityId = ANY (#{facilityIds}::INT[])")
  Integer getInventoryCount(@Param("programId")Long programId, @Param("equipmentTypeId")Long equipmentTypeId, @Param("facilityIds")String facilityIds);

  @Select("SELECT * from equipment_inventories where id = #{id}")
  @Results({
      @Result(property = "equipmentId", column = "equipmentId"),
      @Result(
          property = "equipment", column = "equipmentId", javaType = Equipment.class,
          one = @One(select = "org.openlmis.equipment.repository.mapper.EquipmentMapper.getById")),
      @Result(property = "facilityId", column = "facilityId"),
      @Result(
          property = "facility", column = "facilityId", javaType = Facility.class,
          one = @One(select = "org.openlmis.core.repository.mapper.FacilityMapper.getById"))
  })
  EquipmentInventory getInventoryById(@Param("id") Long id);

  @Insert("INSERT into equipment_inventories " +
      " ( facilityId, equipmentId, programId, serialNumber, isSerialNumberVerified" +
      ", yearOfInstallation, purchasePrice, sourceOfFund, replacementRecommended, reasonForReplacement" +
      ", nameOfAssessor, dateLastAssessed, isActive, dateDecommissioned, hasStabilizer" +
      ", primaryDonorId, createdBy, createdDate, modifiedBy, modifiedDate,nameOfSparePart, remark) " +
      "values " +
      " ( #{facilityId}, #{equipmentId}, #{programId}, #{serialNumber}, #{isSerialNumberVerified}" +
      ", #{yearOfInstallation}, #{purchasePrice}, #{sourceOfFund}, #{replacementRecommended}, #{reasonForReplacement}" +
      ", #{nameOfAssessor}, #{dateLastAssessed}, #{isActive}, #{dateDecommissioned}, #{hasStabilizer}" +
      ", #{primaryDonorId}, #{createdBy}, NOW(), #{modifiedBy}, NOW(), #{nameOfSparePart}, #{remark})")
  @Options(useGeneratedKeys = true)
  void insert(EquipmentInventory inventory);

  @Update("UPDATE equipment_inventories " +
      "SET " +
      " facilityId = #{facilityId}, equipmentId = #{equipmentId}, programId = #{programId}, " +
      " serialNumber = #{serialNumber}, " +
      " isSerialNumberVerified = #{isSerialNumberVerified}, " +
      " yearOfInstallation = #{yearOfInstallation}, purchasePrice = #{purchasePrice}, " +
      " sourceOfFund = #{sourceOfFund}, replacementRecommended = #{replacementRecommended}, " +
      " reasonForReplacement = #{reasonForReplacement}, nameOfAssessor = #{nameOfAssessor}, " +
      " dateLastAssessed = #{dateLastAssessed}, hasStabilizer = #{hasStabilizer} " +
      " , isActive = #{isActive}, dateDecommissioned = #{dateDecommissioned}, primaryDonorId = #{primaryDonorId} " +
      " , modifiedBy = #{modifiedBy}, modifiedDate = NOW(), " +
      " nameOfSparePart = #{nameOfSparePart}, remark = #{remark}" +
      " WHERE id = #{id}")
  void update(EquipmentInventory inventory);

    @Select("Select * from fn_populate_alert_equipment_nonfunctional(1);")
    String updateNonFunctionalEquipments();

    @Select("SELECT  i.id, eq.name as equipmentName, eq.model as model, e.serialNumber as serial, eq.energyTypeId, i.* " +
            " from " +
            " vaccine_report_cold_chain_line_items i " +
            "   join equipment_inventories e on e.id = i.equipmentInventoryId " +
            "   join equipments eq on eq.id = e.equipmentId " +
            " where " +
            " i.equipmentInventoryId = #{equipmentInventoryId} order by i.id")
    List<ColdChainEquipmentTemperatureStatusDTO> getLineItemsByEquipmentInventory(@Param("equipmentInventoryId") Long equipmentInventoryId);

    @Select("SELECT DISTINCT f.* FROM alert_equipment_nonfunctional a left join facilities f on f.id=a.facilityid")
    List<Facility> getFacilitiesWithNonFunctionalEquipments();

    @Delete("Delete from vaccine_report_cold_chain_line_items where equipmentinventoryid=#{inventoryId} ")
    Integer deleteEquipmentFromIDVReport(@Param("inventoryId") Long inventoryId);

    @Delete("Delete from equipment_inventory_statuses where inventoryid=#{inventoryId} ")
    Integer deleteEquipmentInventoryStatuses(@Param("inventoryId") Long inventoryId);

    @Delete("Delete from equipment_inventories where id=#{inventoryId} ")
    Integer deleteEquipmentInventory(@Param("inventoryId") Long inventoryId);

    @Select("select * from equipment_inventories where serialNumber = #{serialNumber} " +
        "order by (case when isSerialNumberVerified = true then 1 else 2 end) asc limit 1")
    EquipmentInventory findInventoryBySerialNumber(@Param("serialNumber") String serialNumber);

    @Select("SELECT COUNT(ei.id)" +
          " FROM equipment_inventories ei" +
          " JOIN equipments e ON ei.equipmentId = e.id" +
          " JOIN equipment_types et ON e.equipmentTypeId = et.id " +
          " JOIN facilities f ON ei.facilityId = f.id " +
          " WHERE ei.programId = #{programId}" +
          " AND et.id = #{equipmentTypeId}" +
          " AND ei.facilityId = ANY (#{facilityIds}::INT[]) AND (LOWER(f.name) LIKE '%' || LOWER(#{searchParam}) || '%')  OR" +
            " (LOWER(e.name) LIKE '%' || LOWER(#{searchParam}) || '%') OR (LOWER(e.model) LIKE '%' || LOWER(#{searchParam}) || '%') OR (LOWER(ei.serialNumber) LIKE '%' || LOWER(#{searchParam}) || '%') ")
    Integer getInventoryCountBySearch(@Param("searchParam") String searchParam,@Param("programId")Long programId, @Param("equipmentTypeId")Long equipmentTypeId, @Param("facilityIds")String facilityIds);

  @Select("SELECT ei.*, (select count(ds.*) > 0 from equipment_daily_cold_trace_status ds where ds.equipmentInventoryId = ei.id) as submittedDailyStatus " +
          " FROM equipment_inventories ei" +
          " JOIN equipments e ON ei.equipmentId = e.id" +
          " JOIN equipment_types et ON e.equipmentTypeId = et.id" +
          " JOIN facilities f ON ei.facilityId = f.id " +
          " WHERE ei.programId = #{programId}" +
          " AND et.id = #{equipmentTypeId} " +
          " AND ei.facilityId = ANY (#{facilityIds}::INT[]) " +
          " AND (LOWER(f.name) LIKE '%' || LOWER(#{searchParam}) || '%')  OR  " +
          "(LOWER(e.name) LIKE '%' || LOWER(#{searchParam}) || '%') OR (LOWER(e.model) LIKE '%' || LOWER(#{searchParam}) || '%') OR (LOWER(ei.serialNumber) LIKE '%' || LOWER(#{searchParam}) || '%') ")
  @Results({
          @Result(property = "equipmentId", column = "equipmentId"),
          @Result(
                  property = "equipment", column = "equipmentId", javaType = Equipment.class,
                  one = @One(select = "org.openlmis.equipment.repository.mapper.EquipmentMapper.getById")),
          @Result(property = "facilityId", column = "facilityId"),
          @Result(
                  property = "facility", column = "facilityId", javaType = Facility.class,
                  one = @One(select = "org.openlmis.core.repository.mapper.FacilityMapper.getById")),
          @Result(property = "coldChainLineItems", javaType = List.class, column = "equipmentInventoryId",
                  many = @Many(select = "getLineItemsByEquipmentInventory"))

  })
  List<EquipmentInventory> searchInventory(@Param("searchParam") String searchParam, @Param("programId")Long programId, @Param("equipmentTypeId")Long equipmentTypeId, @Param("facilityIds")String facilityIds,RowBounds rowBounds);
}
