package org.openlmis.report.builder;

import org.openlmis.report.model.params.MonthlyStockStatusParam;
import org.openlmis.report.model.params.SummaryReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.apache.ibatis.jdbc.SqlBuilder.ORDER_BY;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;


public class MonthlyStockOnHandQueryBuilder {


    private static String getAggregateSelect(MonthlyStockStatusParam param) {
        BEGIN();
        SELECT(
                " li.productCode as code" +
                ", li.product" +
                ", li.productCategory as category" +
                ", li.dispensingUnit as unit" +
                ", sum(li.beginningBalance) as openingBalance" +
                ", sum(li.quantityReceived) as receipts" +
                ", sum(li.quantityDispensed) as issues" +
                ", sum(li.totalLossesAndAdjustments) as adjustments" +
                ", sum(li.stockInHand) as closingBalance " +
                ", sum(li.quantityApproved) as reorderAmount " +
                ", sum(0) as stockOutRate ");
        FROM(
                " facilities   " +
                "    inner join requisitions r ON  r.facilityId = facilities.id   " +
                "    inner join requisition_line_items li ON li.rnrId = r.id    " +
                "    inner join products ON products.code  ::text =   li.productCode  ::text      " +
                "    inner join vw_districts gz on gz.district_id = facilities.geographicZoneId " +
                "    inner join programs ON  r.programId = programs.id " +
                "    inner join program_products pps ON  r.programId = pps.programId and products.id = pps.productId " +
                "    inner join requisition_group_members ON facilities.id = requisition_group_members.facilityId " +
                "    inner join requisition_groups ON requisition_groups.id = requisition_group_members.requisitionGroupId " +
                "    inner join requisition_group_program_schedules ON requisition_group_program_schedules.programId = programs.id   " +
                "               AND requisition_group_program_schedules.requisitionGroupId = requisition_groups.id " +
                "    inner join processing_schedules ON processing_schedules.id = requisition_group_program_schedules.scheduleId  " +
                "    inner join processing_periods ON processing_periods.id = r.periodId  ");

        writePredicates(param);
        GROUP_BY("li.productCode, li.productCategory, li.product, li.dispensingUnit");
        ORDER_BY("productCategory asc, product asc");
        return SQL();
    }

    public static String getQuery(Map params) {
        MonthlyStockStatusParam filter = (MonthlyStockStatusParam) params.get("filterCriteria");
        return getAggregateSelect(filter);
    }

    private static void writePredicates(MonthlyStockStatusParam filter) {

        WHERE(rnrStatusFilteredBy("r.status", "'APPROVED', 'RELEASED'"));
        WHERE(periodIsFilteredBy("r.periodId"));
        WHERE(programIsFilteredBy("r.programId"));

        if (filter.getProductCategory() > 0) {
            WHERE(productCategoryIsFilteredBy("pps.productCategoryId"));
        }

        if (filter.getProduct() > 0) {
            WHERE(productFilteredBy("products.id"));
        }

        if (filter.getZone() > 0) {
            WHERE(geoZoneIsFilteredBy("gz"));
        }


    }



}
