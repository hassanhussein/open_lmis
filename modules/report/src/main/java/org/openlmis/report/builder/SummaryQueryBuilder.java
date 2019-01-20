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

import org.openlmis.report.model.params.SummaryReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.reportTypeFilteredBy;

public class SummaryQueryBuilder {

    private static String getAggregateSelect(SummaryReportParam param) {

        String sql = "SELECT code, \n" +
                "       product, \n" +
                "       category, \n" +
                "       unit, \n" +
                "       Sum(openingBalance) AS openingBalance, \n" +
                "       Sum(receipts)       AS receipts, \n" +
                "       Sum(issues)         AS issues, \n" +
                "       Sum(adjustments)    AS adjustments, \n" +
                "       Sum(closingBalance) AS closingBalance, \n" +
                "       Sum(reorderAmount)  AS reorderAmount, \n" +
                "       Sum(0)              AS stockOutRate \n" +
                "FROM   mv_logistics_summary_report WHERE \n" +
                getPredicate(param) +
                " GROUP BY code, category, product, unit \n" +
                " ORDER BY category asc, product asc ";
        return sql;
    }


    private static String getDisaggregatedSelect(SummaryReportParam param) {

        String sql = " SELECT " +
                "  code " +
                ", product" +
                ", facilityCode" +
                ", facility" +
                ", facilityType" +
                ", category" +
                ", unit" +
                ", openingBalance" +
                ",  receipts" +
                ",  issues" +
                ",  reorderAmount " +
                ", adjustments" +
                ", closingBalance " +
                " FROM mv_logistics_summary_report WHERE " +
                getPredicate(param) +
                " ORDER BY category asc, product asc";
        return sql;
    }


    public static String getQuery(Map params) {
        SummaryReportParam filter = (SummaryReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated()) {
            return getDisaggregatedSelect(filter);
        }
        return getAggregateSelect(filter);
    }

    private static void writePredicates(SummaryReportParam filter) {

        //  WHERE(rnrStatusFilteredBy("r.status", "'AUTHORIZED','APPROVED', 'RELEASED'"));
        WHERE(periodIsFilteredBy("periodid"));
        WHERE(programIsFilteredBy("programid"));

        if (filter.getProductCategory() > 0) {
            WHERE(productCategoryIsFilteredBy("category"));
        }

        if (filter.getProduct() > 0) {
            WHERE(productFilteredBy("productid"));
        }

        if (filter.getZone() > 0) {
            WHERE(geoZoneIsFilteredBy("mv_logistics_summary_report"));
        }

        if (filter.getFacilityType() > 0) {
            WHERE(facilityTypeIsFilteredBy("facility_type_id"));
        }

        if (filter.getFacility() > 0) {
            WHERE(facilityIsFilteredBy("facility_id"));
        }


        if (filter.getAllReportType()) {
            WHERE("emergency in (true,false)");
        } else {

            WHERE(reportTypeFilteredBy("emergency"));
        }

    }


    private static String getPredicate(SummaryReportParam filter) {
        String predicate = "";

        predicate += "  " + periodIsFilteredBy(" periodid ");
        predicate += " AND " + programIsFilteredBy("programid");

        if (filter.getProductCategory() != 0) {
            predicate += " AND " + productCategoryIsFilteredBy("category ");
        }
        if (filter.getProduct() > 0) {
            predicate += " AND " + productFilteredBy("productid");
        }
        if (filter.getZone() != 0) {
            predicate += " AND " + geoZoneIsFilteredBy("mv_logistics_summary_report");
        }
        if (filter.getFacilityType() != 0) {
            predicate += " AND " + facilityTypeIsFilteredBy("facility_type_id");
        }
        if (filter.getFacility() != 0) {
            predicate += " AND " + facilityIsFilteredBy("facility_id");
        }
        if (filter.getAllReportType()) {
            predicate += " AND emergency in (true,false) ";
        } else {
            predicate += " AND " + reportTypeFilteredBy("emergency");
        }

        return predicate;

    }

}
