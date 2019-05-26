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

package org.openlmis.report.builder;

import org.openlmis.report.model.params.AggregateConsumptionReportParam;
import org.openlmis.report.model.params.FacilityConsumptionReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class FacilityConsumptionQueryBuilder extends ConsumptionBuilder {
    private static final String CONSUMPTION_DISPENSED_REPORT_AGGREGATE_SEGEMENT=  " sum(r.dispensed)::integer dispensed\n" ;
    private static final String CONSUMPTION_ADJUSTED_REPORT_AGGREGATE_SEGEMENT=  " sum(r.consumption)::integer dispensed\n" ;

    public static String aggregateCrossTabQuery(FacilityConsumptionReportParam filter) {
        String query = " ( select code[1] as product , code[2] as productCode " +
                filter.getCrossColumnHeader()+
                " from CROSSTAB\n" +
                "('SELECT ARRAY[r.productDispalayName,r.productcode::text] code,  \n" +
                "  r.period::text periodName,\n" +
                (filter.getAdjustedConsumption()?CONSUMPTION_ADJUSTED_REPORT_AGGREGATE_SEGEMENT:CONSUMPTION_DISPENSED_REPORT_AGGREGATE_SEGEMENT)+
                " FROM mv_requisition r\n" +
                writePredicateString(filter) +
                " GROUP BY r.productcode, r.productDispalayName,r.period ,\n" +
                "r.startdate ')  \n" +
                "As facilit_consumption" +
                filter.getCrossTabColumn() +
                " )\n";
        return query;

    }

    public static String aggregateTotalCountQuery(FacilityConsumptionReportParam filter) {
        String query = "";
        BEGIN();
        SELECT(" count(*)");
        FROM(aggregateCrossTabQuery(filter) +" req");
        query = SQL();
        return query;
    }

    public static String disAggregateCrossTabQuery(FacilityConsumptionReportParam filter) {
        String query = " ( select code[1] as facilityid, code[2] as facilitycode, code[3] as facility ,\n" +
                "code[4]  as facilitytype , code[5] as  facProdCode,\n" +
                "code[6] as product , code[7] as productCode " +
                filter.getCrossColumnHeader()+
                "  from\n" +
                "CROSSTAB\n" +
                "('SELECT ARRAY[r.facilityid::text, r.facilitycode,r.facility,\n" +
                "r.facilitytype,r.facprodcode,\n" +
                "r.productDispalayName::text,r.productcode::text] code, \n" +
                "  r.period::text periodName,\n" +
                (filter.getAdjustedConsumption()?CONSUMPTION_ADJUSTED_REPORT_AGGREGATE_SEGEMENT:CONSUMPTION_DISPENSED_REPORT_AGGREGATE_SEGEMENT)+
                " FROM mv_requisition r\n" +
                writePredicateString(filter) +
                " GROUP BY r.facilityid::text, r.facilitycode,r.facility,\n" +
                "r.facilitytype,r.facprodcode,\n" +
                "r.productDispalayName,r.productcode ,r.period ')  \n" +
                "As facilit_consumption" +
                filter.getCrossTabColumn() +
                " )\n";
        return query;

    }

    public static String disAggregateTotalCountQuery(FacilityConsumptionReportParam filter) {
        String query = "";
        BEGIN();
        SELECT(" count(*)");
        FROM(disAggregateCrossTabQuery(filter) +"  req");
        query = SQL();
        return query;
    }

    public static String getAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();
        SELECT("cr.*");
        FROM(aggregateCrossTabQuery(filter) + " cr ");
        String query = SQL();
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;

    }

    public static String getDisAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();

        SELECT("cr.*");
        FROM(disAggregateCrossTabQuery(filter) + " cr ");
        String query = SQL();
        //writePredicates(filter);
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;

    }


    public static String getQuery(Map params) {

        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelect(filter);
        else
            return getAggregateSelect(filter);

    }
    public static String getTotalCountQuery(Map params) {

        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return disAggregateTotalCountQuery(filter);
        else
            return aggregateTotalCountQuery(filter);

    }

}
