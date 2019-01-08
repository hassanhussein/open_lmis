package org.openlmis.report.builder;


import org.openlmis.report.model.params.AggregateStockStatusReportParam;

import java.util.Map;

import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class AggregateStockStatusReportQueryBuilder {

    public static String getQuery(Map params) {

        AggregateStockStatusReportParam filter = (AggregateStockStatusReportParam) params.get("filterCriteria");
        Long userId = (Long) params.get("userId");

        return ("SELECT \n" +
                "(SELECT fn_get_stock_color(y.status)) as color, *\n" +
                " FROM (\n" +
                "\n" +
                "SELECT ROW_NUMBER() OVER (PARTITION BY facilityId,period ORDER BY period asc) AS r,x.* from \n" +
                "(\n" +
                "select *,req_status requisitionStatus,\n" +
                "\n" +
                "facility_id facilityId,facility,facilitytypename,location district,periodId period, processing_period_name periodName, stockInHand soh, mos,amc   \n" +
                "from vw_stock_status \n" +
                " inner JOIN vw_districts gz on gz.district_id = gz_id \n" +
                " WHERE " + writePredicates(filter, userId) +
                " order by periodId asc\n" +
                ")x\n" +
                ")Y");
    }


    private static String writePredicates(AggregateStockStatusReportParam filter, Long userId) {

        String predicate = "";
        predicate += " req_status NOT IN('INITIATED','SUBMITTED') AND " + programIsFilteredBy("programId") +
        " AND " + periodStartDateRangeFilteredBy("startdate", filter.getPeriodStart().trim()) +
        " AND " + periodEndDateRangeFilteredBy("enddate", filter.getPeriodEnd().trim());

        if (filter.getProduct() != 0) {
            predicate += " AND " + productFilteredBy("productId");
        }
        if (filter.getZone() != 0) {
            predicate += " AND " + geoZoneIsFilteredBy("gz");
        }
        if (filter.getSchedule() != 0) {
            predicate += " AND " + scheduleFilteredBy("psid");
        }
        return predicate;
    }


}
