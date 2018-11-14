package org.openlmis.report.builder;

import org.openlmis.report.model.params.AggregateStockStatusReportParam;

import java.util.Map;

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
                "where programid = 8 and productId = 2496 and gz_id =504 and req_status NOT IN('INITIATED','SUBMITTED')\n" +
                " order by periodid asc\n" +
                ")x\n" +
                ")Y");
    }


}
