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


import org.openlmis.report.model.params.BaseParam;
import org.openlmis.report.model.params.FacilityConsumptionReportParam;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class ConsumptionBuilder {

    public static void writeBasicQuery() {
        SELECT("r.productcode code");
        SELECT("r.period periodName");
        SELECT("r.startdate periodStart");
        SELECT("r.productdispalayname as product");
        SELECT("sum(r.dispensed) dispensed");
        SELECT("sum(r.consumption) consumption");
        SELECT("sum(r.amc) amc");
        SELECT(" round(100*((sum(r.consumption) - COALESCE(sum(r.amc), 0)) /" +
                " COALESCE(NULLIF(avg(r.amc), 0), 1))::numeric, 4) AS consumptionrate");
        SELECT("( SELECT df.description" +
                "           FROM data_range_flags_configuration df\n" +
                "          WHERE LOWER(category)= 'consumption' and df.range @> round(100*\n" +
                "  (" +
                "sum(r.consumption)- COALESCE(sum(r.amc), 0)) / COALESCE(NULLIF(sum(r.amc), 0), 1)\n" +
                " , 4)::numeric) AS flagcolor");
        SELECT("ceil(sum(r.dispensed/r.packsize)::float) consumptionInPacks");
        SELECT("ceil(sum(r.consumption/r.packsize)::float) adjustedConsumptionInPacks ");
    }

    public static void writeCommonJoinStatment() {
        FROM("mv_requisition r");
    }

    protected static void writePredicates(FacilityConsumptionReportParam filter) {

        WHERE(programIsFilteredBy("r.programid"));
        WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
        if (filter.getPeriodStart() != null)
            WHERE(periodStartDateRangeFilteredBy("r.startdate", filter.getPeriodStart().trim()));
        if (filter.getPeriodEnd() != null)
            WHERE(periodEndDateRangeFilteredBy("r.enddate", filter.getPeriodEnd().trim()));
        if (filter.getPeriod() != null)
            WHERE(periodIsFilteredBy("r.periodid"));
        if (filter.getFacility() != null && filter.getFacility() != 0) {
            WHERE(facilityIsFilteredBy("r.facilityid"));
        }
        if (filter.getZone() != null && filter.getZone() != 0) {
            WHERE(geoZoneIsFilteredBy("r.provinceid", "r.parent", "r.zoneid", "r.districtid"));
        }
        if (filter.getProductCategory() != null && filter.getProductCategory() != 0) {
            WHERE("  r.categoryid =" + filter.getProductCategory());
        }
        if (filter.getProducts() != null&&!"0".equals(filter.getProducts()) && !filter.getProducts().isEmpty()&& !filter.getProducts().trim().equalsIgnoreCase("")) {
            WHERE(multiProductFilterBy(filter.getProducts(),"r.productid","r.tracer"));
        }
        if (filter.getAllReportType()) {
            WHERE("r.emergency in (true,false)");
        } else {
            WHERE(reportTypeFilteredBy("r.emergency"));
        }
    }

    public static String writePredicateString(FacilityConsumptionReportParam filter) {

        StringBuilder predicate = new StringBuilder();
        predicate.append(" where r.programid=" + filter.getProgram());
        predicate.append(" and r.status in (" + filter.getAcceptedRnrStatuses() + ")");
        predicate.append(" and r.startdate::date>='" + filter.getPeriodStart() + "'::date");
        predicate.append(" and  r.enddate::date<='" + filter.getPeriodEnd() + "'::date");
        if (filter.getFacility() != null && filter.getFacility() != 0) {
            predicate.append(" and r.facilityid =" + filter.getFacility());
        }
        if (filter.getZone() != null && filter.getZone() != 0) {
            predicate.append(" and (r.zoneid=" + filter.getZone() + " or r.provinceid=" +
                    filter.getZone() + " or r.districtid=" + filter.getZone() + ")");
        }
        if (filter.getProductCategory() != null && filter.getProductCategory() != 0) {
            predicate.append(" and r.categoryid =" + filter.getProductCategory());
        }
        if (filter.getProducts() != null&&!"0".equals(filter.getProducts()) && !filter.getProducts().isEmpty()&& !filter.getProducts().trim().equalsIgnoreCase("")) {
            predicate.append(" and " +multiProductFilterBy(filter.getProducts(),"r.productid","r.tracer"));
        }
        if (filter.getAllReportType()) {
            predicate.append(" and r.emergency in (true,false)");
        } else {
            predicate.append(" and r.emergency =" + filter.getIsEmergency());
        }
        if(filter.getFeFacility()){
            predicate.append(" and r.feconfigured =" + filter.getFeFacility());
        }
        return predicate.toString();
    }

    public static String getOrderString(BaseParam filter) {
        String sortString = "";
        sortString = (filter.getSortBy() != null && filter.getSortBy().trim().length() > 0) ? filter.getSortBy() : " productcode ";
        sortString = sortString + " " + (filter.getSortDirection() != null && filter.getSortDirection().trim().length() > 0 ? filter.getSortDirection() : " asc ");
        return sortString;
    }
}
