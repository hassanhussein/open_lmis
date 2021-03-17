/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.logging.repository.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.openlmis.logging.domain.DatabaseTable;

import java.util.List;

public interface DataTableMapper {

    @Select("SELECT *\n" +
            "FROM pg_catalog.pg_tables\n" +
            "WHERE schemaname != 'pg_catalog' AND \n" +
            "    schemaname != 'information_schema';")
    @Results(value = {
            @Result(property = "schemaName", column = "schemaname"),
            @Result(property = "tableName", column = "tablename"),
            @Result(property = "tableOwner", column = "tableowner"),
            @Result(property = "tableSpace", column = "tablespace"),
            @Result(property = "hasIndexes", column = "hasindexes"),
            @Result(property = "hasRules", column = "hasrules"),
            @Result(property = "hasTriggers", column = "hastriggers"),
            @Result(property = "rowSecurity", column = "rowsecurity")
    })
    public List<DatabaseTable> loadTableList();
}
