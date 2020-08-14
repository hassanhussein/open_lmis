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

package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.core.domain.ManualTestResultCategory;
import org.openlmis.core.domain.ManualTestType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualTestResultCategoryMapper {

    @Select("select * from manual_test_result_category order by displayorder")
    List<ManualTestResultCategory> getAll();

    @Select("select * from manual_test_result_category where id = #{id}")
    ManualTestResultCategory getById(Long id);

    @Insert("insert into manual_test_result_category (code, name, description, displayorder,createdBy,createdDate, modifiedBy, modifiedDate) " +
            " values (#{code}, #{name},#{description}, #{displayOrder}, #{createdBy}, COALESCE(#{createdDate}, NOW()), #{modifiedBy}, COALESCE(#{modifiedDate}, NOW()))")
    void insert(ManualTestResultCategory type);

    @Update("update manual_test_result_category set code=#{code}, name=#{name},description =#{description}, displayorder = #{displayOrder}, " +
            " modifiedBy = #{modifiedBy}, modifiedDate = COALESCE(#{modifiedDate}, NOW()) where id = #{id}")
    void update(ManualTestResultCategory type);

    @Delete("delete from manual_test_result_category where id = #{id}")
    void remove(Long id);
}
