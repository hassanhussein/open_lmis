/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 *   Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 *   This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.report.builder;

import org.openlmis.report.model.params.FacilityConsumptionReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.multiProductFilterBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.reportTypeFilteredBy;



public class RequistionStatusReportsBuilder {
    public static String getRequisitionListCountQuery(Map params){
        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        String query = "";
        BEGIN();
        SELECT("count(*)");
        FROM("mv_requisition");
        writePredicates(filter);
        query = SQL();
        return query;
    }
    public static String getQuery(Map params) {

        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        String query = "";
        BEGIN();
        SELECT("rnrid");
        SELECT("status");
        SELECT("emergency");
        SELECT("zoneid");
        SELECT("district");
        SELECT("districtid");
        SELECT("provinceid");
        SELECT("province");
        SELECT("facility");
        SELECT("facilitycode");
        SELECT("facilityid");
        SELECT("feconfigured");
        SELECT("facilitytypeid");
        SELECT("facilitytype");
        SELECT("program");
        SELECT("programid");
        SELECT("periodid");
        SELECT("period");
        SELECT("startdate");
        SELECT("enddate");
        SELECT("productcategory");
        FROM("mv_requisition r");
        writePredicates(filter);
        query = SQL();
        return query;



    }

    protected static void writePredicates(FacilityConsumptionReportParam filter) {

        WHERE(programIsFilteredBy("r.programid"));
        WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
        if (filter.getStatus() != null && !filter.getStatus().trim().equals("")) {
            WHERE("r.status=#{filterCriteria.status}");
        }
        if (filter.getPeriodStart() != null)
            WHERE(periodStartDateRangeFilteredBy("r.startdate", filter.getPeriodStart().trim()));
        if (filter.getPeriodEnd() != null)
            WHERE(periodEndDateRangeFilteredBy("r.enddate", filter.getPeriodEnd().trim()));
        if (filter.getFacility() != null && filter.getFacility() != 0) {
            WHERE(facilityIsFilteredBy("r.facilityid"));
        }
        if (filter.getZone() != null && filter.getZone() != 0) {
            WHERE(geoZoneIsFilteredBy("r.provinceid", "r.parent", "r.zoneid", "r.districtid"));
        }

        if (filter.getAllReportType()) {
            WHERE("r.emergency in (true,false)");
        } else {
            WHERE(reportTypeFilteredBy("r.emergency"));
        }
    }
}
