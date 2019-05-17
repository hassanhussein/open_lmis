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


import org.openlmis.report.model.params.FacilityConsumptionReportParam;
import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.INNER_JOIN;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class ConsumptionBuilder{

    public static void writeCommonJoinStatment() {
        FROM("requisition_line_items li");
        INNER_JOIN("requisitions r on r.id = li.rnrid");
        INNER_JOIN("facilities f on r.facilityId = f.id ");
        INNER_JOIN("vw_districts d on d.district_id = f.geographicZoneId ");
        INNER_JOIN("processing_periods pp on pp.id = r.periodId");
        INNER_JOIN("products p on p.code::text = li.productCode::text");
        INNER_JOIN("program_products ppg on ppg.programId = r.programId and ppg.productId = p.id");
        INNER_JOIN("dosage_units ds ON ds.id = p.dosageunitid");
    }

    protected static void writePredicates(FacilityConsumptionReportParam filter) {

        WHERE(programIsFilteredBy("r.programId"));
        WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
        WHERE(periodStartDateRangeFilteredBy("pp.startdate", filter.getPeriodStart().trim()));
        WHERE(periodEndDateRangeFilteredBy("pp.enddate", filter.getPeriodEnd().trim()));
        if (filter.getFacility() != null && filter.getFacility() != 0) {
            WHERE(facilityIsFilteredBy("f.id"));
        }
        if (filter.getZone() != 0) {
            WHERE(geoZoneIsFilteredBy("d"));
        }

        if (filter.getAllReportType()) {
            WHERE("r.emergency in (true,false)");
        } else {
            WHERE(reportTypeFilteredBy("r.emergency"));
        }
    }


    public static String getOrderString(FacilityConsumptionReportParam filter) {
        String sortString = "";
        sortString = (filter.getSortBy() != null && filter.getSortBy().trim().length() > 0) ? filter.getSortBy() : " p.primaryName ";
        sortString = sortString+" " + (filter.getSortDirection() != null && filter.getSortDirection().trim().length() > 0 ? filter.getSortDirection() : " asc ");
        return sortString;
    }
}
