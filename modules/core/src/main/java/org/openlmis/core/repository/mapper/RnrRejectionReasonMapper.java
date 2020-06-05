
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

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.RnrRejectionReasons;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RnrRejectionReasonMapper {

    @Insert({"INSERT INTO requisition_rejection_reasons" +
            "(code, name, description, regular_rnr, emergency_rnr )" +
            " values (#{code},#{name}, #{description},#{regularRnr},#{emergencyRnr})"})
    void insert(RnrRejectionReasons reasons);

    @Update({"update requisition_rejection_reasons" +
            " set code=#{code}," +
            " name=#{name}, " +
            "description=#{description}, " +
            "regular_rnr=#{regularRnr}, " +
            "emergency_rnr=#{emergencyRnr}" +
            "  where id = #{id}"})
    void update(RnrRejectionReasons reasons);

    @Select({"SELECT * FROM requisition_rejection_reasons "})
    @Results(value = {
            @Result(property = "code", column = "code"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "regularRnr", column = "regular_rnr"),
            @Result(property = "emergencyRnr", column = "emergency_rnr")
    })
    List<RnrRejectionReasons> getAll();

    @Select({"SELECT * FROM requisition_rejection_reasons " +
            "where code = #{code}"})
    @Results(value = {
            @Result(property = "code", column = "code"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "regularRnr", column = "regular_rnr"),
            @Result(property = "emergencyRnr", column = "emergency_rnr")
    })
    RnrRejectionReasons getByCode(RnrRejectionReasons record);
}
