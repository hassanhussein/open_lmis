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


import org.openlmis.report.model.params.StockImbalanceReportParam;
import org.openlmis.report.model.report.StockImbalanceReport;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class StockImbalanceQueryBuilder {

    public static final  String FILTER_CRITERIA_LABEL= "filterCriteria";
    public static String getQuery(Map params) {


        StockImbalanceReportParam filter = (StockImbalanceReportParam) params.get(FILTER_CRITERIA_LABEL);
        Map sortCriteria = (Map) params.get("SortCriteria");
        BEGIN();
        SELECT("distinct supplyingFacility, facilityTypeName facilityType,  facility, facilityCode, d.district_name districtName, d.region_name as regionName, d.zone_name zoneName, product, productCode,  stockInHand as physicalCount,  amc,  mos months,  required, ordered as orderQuantity, " +
                "status");
        FROM("  vw_stock_status " +
                        " join facilities f on f.id = facility_id " +
                        " join vw_districts d on d.district_id = f.geographicZoneId " +
                        "join facility_types ft on f.typeid=ft.id "
        );
        WHERE("status in ('" + filter.getStatus().replaceAll(",", "','") + "')");
        WHERE(rnrStatusFilteredBy("req_status", filter.getAcceptedRnrStatuses()));
        WHERE("(amc != 0 or stockInHand != 0 or reported_figures > 0)");
        WHERE(periodIsFilteredBy("periodId"));
        WHERE(programIsFilteredBy("vw_stock_status.programId"));
        WHERE(userHasPermissionOnFacilityBy("facility_id"));
        if (filter.getFacilityType() != 0) {
            WHERE(facilityTypeIsFilteredBy("vw_stock_status.facilityTypeId"));
        }

        if (filter.getFacility() != 0) {
            WHERE(facilityIsFilteredBy("facility_id"));
        }
        if (filter.getProductCategory() != 0) {
            WHERE(productCategoryIsFilteredBy("vw_stock_status.categoryId"));
        }

        if (multiProductFilterBy(filter.getProducts(), "vw_stock_status.productId", "indicator_product") != null) {
            WHERE(multiProductFilterBy(filter.getProducts(), "vw_stock_status.productId", "indicator_product"));
        }

        if (filter.getZone() != 0) {
            WHERE(geoZoneIsFilteredBy("d"));
        }
        ORDER_BY(QueryHelpers.getSortOrder(sortCriteria, StockImbalanceReport.class, "supplyingFacility asc, facility asc, product asc"));
        return SQL() + " limit 20000";
    }

    public static String getReportQuery(Map params) {
        StockImbalanceReportParam filter = (StockImbalanceReportParam) params.get(FILTER_CRITERIA_LABEL);
        return "SELECT  \n" +
                "supplyingFacility, \n" +
                "facilityTypeName facilityType,  \n" +
                "facility, \n" +
                "facilityCode, \n" +
                "a.district_name districtName,\n" +
                "a.region_name as regionName, \n" +
                "a.zone_name zoneName, \n" +
                "product, productCode,  \n" +
                "a.stockInHand as physicalCount,\n" +
                "a.amc,  \n" +
                "a.processing_period_name as period, \n"+
                "a.mos months,  \n" +
                "a.required, \n" +
                "a.ordered as orderQuantity, \n" +
                "a.status\n" +
                "\n" +
                " FROM ( SELECT * from mv_stock_imbalance_by_facility_report WHERE \n" +
                getPredicate(filter) + " ) a \n" +
                " WHERE status in ('" + filter.getStatus().replaceAll(",", "','") + "')" +
                " ORDER BY supplyingFacility asc, facility asc, product asc";

    }

    private static String getPredicate(StockImbalanceReportParam filter) {
        String predicate = "";
        String reportType = filter.getReportType().replaceAll(",", "','").replaceAll("EM", "t").replaceAll("RE", "f");

        predicate += "  " + periodIsFilteredBy(" periodid ");
        predicate += " AND " + programIsFilteredBy("programId");
        predicate += " AND " + userHasPermissionOnFacilityBy("facility_id");
        if (filter.getFacilityType() != 0) {
            predicate += " AND " + facilityTypeIsFilteredBy("facility_type_id");
        }

        if (filter.getFacility() != 0) {
            predicate += " AND " + facilityIsFilteredBy("facility_id");
        }
        if (filter.getProductCategory() != 0) {
            predicate += " AND " + productCategoryIsFilteredBy("productcategoryid ");
        }
      if (multiProductFilterBy(filter.getProducts(), "mv_stock_imbalance_by_facility_report.productId", "indicator_product") != null) {
            predicate += " AND " + multiProductFilterBy(filter.getProducts(), "mv_stock_imbalance_by_facility_report.productId", "indicator_product");
        }

        if (filter.getZone() != 0) {
            predicate += " AND " + geoZoneIsFilteredBy("mv_stock_imbalance_by_facility_report");
        }
        return  predicate +
                " and emergency in ('" + reportType + "')\n" +
                " AND stockinhand IS NOT NULL AND skipped = false";

    }

    public static String getTotalNumberOfRowsQuery(Map params) {

        StockImbalanceReportParam filter = (StockImbalanceReportParam) params.get(FILTER_CRITERIA_LABEL);
        BEGIN();
        SELECT(" count(*) totalRecords");
        FROM(" mv_stock_imbalance_by_facility_report ");
        getPredicate(filter);
        return SQL();
    }
}
