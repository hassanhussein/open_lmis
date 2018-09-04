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

public class FacilityConsumptionQueryBuilder {

    public static String getAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();
        SELECT("p.code");
        SELECT("pp.name periodName");
        SELECT("pp.startdate periodStart");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks ");
        FROM("requisition_line_items li");
        INNER_JOIN("requisitions r on r.id = li.rnrid");
        INNER_JOIN("facilities f on r.facilityId = f.id ");
        INNER_JOIN("vw_districts d on d.district_id = f.geographicZoneId ");
        INNER_JOIN("processing_periods pp on pp.id = r.periodId");
        INNER_JOIN("products p on p.code::text = li.productCode::text");
        INNER_JOIN("program_products ppg on ppg.programId = r.programId and ppg.productId = p.id");
        INNER_JOIN("dosage_units ds ON ds.id = p.dosageunitid");

        writePredicates(filter);

        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate");
        ORDER_BY("p.primaryName,pp.startdate");
        return SQL();

    }
    public static String getDisAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();
        SELECT("p.code");
        SELECT("pp.name periodName");
        SELECT("pp.startdate periodStart");
        SELECT("f.id facilityId");
        SELECT("f.name facility");
        SELECT("ft.name facilityType ");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks ");
        FROM("requisition_line_items li");
        INNER_JOIN("requisitions r on r.id = li.rnrid");
        INNER_JOIN("facilities f on r.facilityId = f.id ");
        INNER_JOIN("vw_districts d on d.district_id = f.geographicZoneId ");
        INNER_JOIN("processing_periods pp on pp.id = r.periodId");
        INNER_JOIN("products p on p.code::text = li.productCode::text");
        INNER_JOIN("program_products ppg on ppg.programId = r.programId and ppg.productId = p.id");
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");
        INNER_JOIN("dosage_units ds ON ds.id = p.dosageunitid");


        writePredicates(filter);

        GROUP_BY("f.id,f.name, ft.name,p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate");
        ORDER_BY("f.name,p.primaryName,pp.startdate");
        return SQL();

    }

    private static void writePredicates(FacilityConsumptionReportParam filter) {

        WHERE(programIsFilteredBy("r.programId"));
//        WHERE(userHasPermissionOnFacilityBy("r.facilityId"));
        WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
        WHERE(periodStartDateRangeFilteredBy("pp.startdate", filter.getPeriodStart().trim()));
        WHERE(periodEndDateRangeFilteredBy("pp.enddate", filter.getPeriodEnd().trim()));
        if(filter.getFacility()!=null&&filter.getFacility()!=0) {
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

    public static String getQuery(Map params) {

        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelect(filter);
        else
            return getAggregateSelect(filter);

    }

}