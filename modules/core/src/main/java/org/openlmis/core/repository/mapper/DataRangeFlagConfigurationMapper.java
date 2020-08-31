/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */


package org.openlmis.core.repository.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.core.domain.DataRangeFlagConfiguration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRangeFlagConfigurationMapper {

    @Insert("insert into data_range_flags_configuration " +
            "(category, rangename,  displayname,minvalue, maxvalue,range, description, createdby,createddate," +
            " modifiedby,modifieddate) " +
            "values" +
            "(#{category}, #{rangename}, #{displayname}, #{minvalue}, #{maxvalue},#{range}::numrange, #{description}, #{createdBy}, #{createdDate}," +
            "#{modifiedBy},#{modifiedDate})")
    public Integer insert(DataRangeFlagConfiguration configuration);

    @Update(" update data_range_flags_configuration" +
            "  set category=#{category}," +
            "      rangename=#{rangename}," +
            "      displayname=#{displayname}," +
            "      minvalue=#{minvalue}," +
            "      maxvalue=#{maxvalue}," +
            "      range = #{range}::numrange," +
            "      description=#{description}," +
            "      modifiedby=#{modifiedBy}," +
            "      modifieddate=#{modifiedDate}" +
            "      where  id =#{id}")
    public void update(DataRangeFlagConfiguration configuration);

    @Delete(" Delete from data_range_flags_configuration where id=#{id}")
    public void delete(DataRangeFlagConfiguration configuration);

    @Select("Select * from data_range_flags_configuration where from id =#{id}")
    public DataRangeFlagConfiguration getById(Long id);

    @Select("Select * from data_range_flags_configuration ")
    public List<DataRangeFlagConfiguration> getAll();
}
