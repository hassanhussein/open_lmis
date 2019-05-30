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
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.equipment.domain.EquipmentType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentTypeMapper {


  @Select("select * from equipment_types where id = #{id}")
  EquipmentType getEquipmentTypeById(Long id);

  @Select("select * from equipment_types order by name")
  List<EquipmentType> getAll();

  @Insert("insert into equipment_types (code, name, createdBy, createdDate, modifiedBy, modifiedDate, isColdChain, IsBioChemistry, categoryId) " +
      " values " +
      " (#{code}, #{name}, #{createdBy},COALESCE(#{createdDate}, NOW()), #{modifiedBy}, NOW(), #{isColdChain}, #{isBioChemistry}," +
          "#{category.id})")
  @Options(useGeneratedKeys = true)
  void insert(EquipmentType type);

  @Update("UPDATE equipment_types SET " +
      "name = #{name}, code = #{code}, modifiedBy = #{modifiedBy}, modifiedDate = NOW(),isColdChain = #{isColdChain}" +
          " , IsBioChemistry = #{isBioChemistry}, categoryId = #{category.id} " +
      " WHERE id = #{id}")
  void update(EquipmentType type);

  @Select("select * from equipment_types where code = #{code}")
  EquipmentType getByCode(String code);
}
