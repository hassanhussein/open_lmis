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

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.ManualTestResultType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualTestResultTypeMapper {

    @Select("select * from manual_test_result_type order by displayorder")
    @Results(value = {
            @Result(property = "testResultCategory", column = "resultcategoryid", javaType = Long.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ManualTestResultCategoryMapper.getById"))})
    List<ManualTestResultType> getAll();

    @Select("select * from manual_test_result_type where id = #{id}")
    @Results(value = {
            @Result(property = "testResultCategory", column = "resultcategoryid", javaType = Long.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ManualTestResultCategoryMapper.getById"))})
    ManualTestResultType getById(Long id);

    @Insert("insert into manual_test_result_type (code, name,resultcategoryid,description, displayorder,createdBy,createdDate, modifiedBy, modifiedDate) " +
            " values (#{code}, #{name},#{testResultCategory.id},#{description}, #{displayOrder}, #{createdBy}, COALESCE(#{createdDate}, NOW()), #{modifiedBy}, COALESCE(#{modifiedDate}, NOW()))")
    void insert(ManualTestResultType type);

    @Update("update manual_test_result_type set code=#{code}, name=#{name}," +
            " resultcategoryid=#{testResultCategory.id}," +
            " description =#{description}, displayorder = #{displayOrder}, " +
            " modifiedBy = #{modifiedBy}, modifiedDate = COALESCE(#{modifiedDate}, NOW()) where id = #{id}")
    void update(ManualTestResultType type);

    @Delete("delete from manual_test_result_type where id = #{id}")
    void remove(Long id);
}
