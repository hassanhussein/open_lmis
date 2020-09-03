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


import org.openlmis.report.model.params.DistrictConsumptionReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class DistrictConsumptionQueryBuilder {

  public static String getDistrictConsumptionQuery(Map params) {

    DistrictConsumptionReportParam filter = (DistrictConsumptionReportParam) params.get("filterCriteria");

    BEGIN();
    SELECT("r.productcode code");
    SELECT("r.product as product");
    SELECT("r.district as district");
    SELECT(" r.districtid as district_id ");
    SELECT("sum(r.dispensed) dispensed");
    SELECT("sum(r.consumption) consumption");
    SELECT("ceil(sum(r.dispensed) / (sum(r.packsize)/count(r.productcode))::float) consumptionInPacks");
    SELECT("ceil(sum(r.consumption) / (sum(r.packsize)/count(r.productcode))::float) adjustedConsumptionInPacks ");
    SELECT("sum(r.amc) amc");
    SELECT(" round(100*(sum(r.consumption) - COALESCE(sum(r.amc), 0)) /" +
            " COALESCE(NULLIF(sum(r.amc), 0), 1)::numeric, 4) AS consumptionrate");
    SELECT("( SELECT df.description" +
            "           FROM data_range_flags_configuration df\n" +
            "          WHERE LOWER(category)= 'consumption' and df.range @> round(100*\n" +
            "  (" +
            "sum(r.consumption)- COALESCE(sum(r.amc), 0)) / COALESCE(NULLIF(sum(r.amc), 0), 1)\n" +
            " , 4)::numeric) AS flagcolor");
    FROM("mv_requisition r");
    WHERE(programIsFilteredBy("r.programid"));
    WHERE(userHasPermissionOnFacilityBy("r.facilityid"));
    WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
    WHERE(productFilteredBy("r.productid"));
    WHERE(periodStartDateRangeFilteredBy("r.startdate", filter.getPeriodStart().trim()));
    WHERE(periodEndDateRangeFilteredBy("r.enddate", filter.getPeriodEnd().trim()));

    if(filter.getProductCategory() != 0){
      WHERE( productCategoryIsFilteredBy("r.categoryid"));
    }

    if (filter.getZone() != 0) {
      WHERE( geoMvZoneIsFilteredBy("r") );
    }

    if(filter.getExcludeDHO())
      WHERE("r.facilitytypeid not in (select id from facility_types where code in ('DHO','DHTM', 'MSL')) "); // exclude DHOs and DHMTs

    GROUP_BY("r.productcode, r.product, r.district, r.districtid");
    return String.format( "select sq.*, " +
        " (sq.consumption / sum(sq.consumption) over ()) * 100 as totalPercentage " +
        "from ( %s ) as sq " +
        "order by coalesce(sq.consumption,0) desc", SQL());
  }

  public static String getFacilityConsumptionQuery(Map params) {
    DistrictConsumptionReportParam filter = (DistrictConsumptionReportParam) params.get("filterCriteria");

    BEGIN();
    SELECT("r.productcode code");
    SELECT("r.product as product");
    SELECT("r.district as district");
    SELECT(" r.districtid as district_id ");
    SELECT(" r.facility as facility ");
    SELECT("sum(r.dispensed) dispensed");
    SELECT("sum(r.consumption) consumption");
    SELECT("ceil(sum(r.dispensed) / (sum(r.packsize)/count(r.productcode))::float) consumptionInPacks");
    SELECT("ceil(sum(r.consumption) / (sum(r.packsize)/count(r.productcode))::float) adjustedConsumptionInPacks ");
    SELECT("sum(r.amc) amc");
    SELECT(" round(100*(sum(r.consumption) - COALESCE(sum(r.amc), 0)) /" +
            " COALESCE(NULLIF(sum(r.amc), 0), 1)::numeric, 4) AS consumptionrate");
    SELECT("( SELECT df.description" +
            "           FROM data_range_flags_configuration df\n" +
            "          WHERE LOWER(category)= 'consumption' and df.range @> round(100*\n" +
            "  (" +
            "sum(r.consumption)- COALESCE(sum(r.amc), 0)) / COALESCE(NULLIF(sum(r.amc), 0), 1)\n" +
            " , 4)::numeric) AS flagcolor");
    FROM("mv_requisition r");

    WHERE(programIsFilteredBy("r.programid"));
    WHERE(userHasPermissionOnFacilityBy("r.facilityid"));
    WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
    WHERE(productFilteredBy("r.productid"));
    WHERE(periodStartDateRangeFilteredBy("r.startdate", filter.getPeriodStart().trim()));
    WHERE(periodEndDateRangeFilteredBy("r.enddate", filter.getPeriodEnd().trim()));

    if(filter.getProductCategory() != 0){
      WHERE( productCategoryIsFilteredBy("r.categoryid"));
    }

    if (filter.getZone() != 0) {
      WHERE( geoMvZoneIsFilteredBy("r") );
    }

    if(filter.getExcludeDHO())
      WHERE("r.facilitytypeid not in (select id from facility_types where code in ('DHO','DHTM', 'MSL')) "); // exclude DHOs and DHMTs

    GROUP_BY("r.productcode, r.product, r.district, r.districtid, r.facility");
    return String.format( "select sq.*, " +
            " (sq.consumption / sum(sq.consumption) over ()) * 100 as totalPercentage " +
            "from ( %s ) as sq " +
            "order by district, facility, product desc", SQL());
  }

}
