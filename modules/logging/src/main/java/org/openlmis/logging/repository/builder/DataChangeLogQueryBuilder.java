/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.logging.repository.builder;

import org.openlmis.logging.domain.params.DataTransactionSearchParameter;

import java.util.Map;
import java.util.UUID;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class DataChangeLogQueryBuilder {
    public static void basicSearchQuery() {
        SELECT("uuid");
        SELECT("description");
        SELECT("createdby");
        SELECT("createddate");
        SELECT("modifiedby");
        SELECT("modifieddate");
        FROM(" logging.t_history_batch t");
    }

    public static String getSearchQuery(Map params) {
        DataTransactionSearchParameter filter = (DataTransactionSearchParameter) params.get("filterCriteria");
        String query;
        BEGIN();
        basicSearchQuery();
        predicate(filter);
        query = SQL();
        return query;

    }

    public static String getTransactionDate( Map params) {
        DataTransactionSearchParameter filter = (DataTransactionSearchParameter) params.get("filterCriteria");
        String query;
        BEGIN();
        basicSearchQuery();
        predicateForTransactionDate(filter);
        query = SQL();
        return query;
    }

    public static void predicate(DataTransactionSearchParameter filter) {


        if (filter.getStartDate() != null) {
            WHERE("t.createddate::date >=#{filterCriteria.startDate}::date");
        }
        if (filter.getEndDate() != null) {
            WHERE("t.createddate::date <#{filterCriteria.endDate}::date");
        }
    }

    public static void predicateForTransactionDate(DataTransactionSearchParameter filter) {

        if (filter.getUuid() != null) {
            WHERE("#{filterCriteria.uuid, typeHandler=org.openlmis.core.utils.UUIDTypeHandler}::UUID= t.uuid::UUID");
        }

    }

    public static String getBachTransactions(UUID  filter) {
        String query = null;
        BEGIN();
        SELECT("id");
        SELECT("data_change_batch_uuid");
        SELECT("data_change_batch_id");
        SELECT("tstamp");
        SELECT("schemaname");
        SELECT("tabname");
        SELECT("operation");
        SELECT("who");
        SELECT("new_val");
        SELECT("old_val");
        SELECT("sql");
        SELECT("raw_data");
        FROM("logging.t_history t");
        writePredicate(filter);
        query = SQL();
        return query;
    }

    public static void writePredicate(UUID filter) {
        WHERE("t.data_change_batch_uuid = '"+filter+"'::UUID");
    }
}
