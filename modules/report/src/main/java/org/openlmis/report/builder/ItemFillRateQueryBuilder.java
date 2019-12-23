package org.openlmis.report.builder;

import org.openlmis.report.model.params.ItemFillRateReportParam;

import java.util.Map;

public class ItemFillRateQueryBuilder {

    public static String getQuery(Map params) {

        ItemFillRateReportParam  filter = (ItemFillRateReportParam) params.get("filterCriteria");

        return
                        " select zone_name zoneName,region_name region, district_name district,f.name facilityName,item.productCode, item.product, " +
                        " to_char(R.modifieddate,'dd-MM-yyyy') orderedDate,to_char(pod.INVOICEDATE,'dd-MM-yyyy') shippedDate, " +
                        " to_char(receiveddate ,'dd-MM-yyyy') receiveddate, coalesce(item.quantityApproved,0) OrderedQuantity, li.quantityShipped quantityReceived, " +
                        " CASE WHEN coalesce(item.quantityApproved,0) > 0 THEN ROUND((LI.quantityShipped *100/ item.quantityApproved ) ,0)  else 0 end as itemFillRate " +
                        " from pod \n" +
                        " JOIN pod_line_items li ON pod.id = li.podid \n" +
                        " JOIN facilities f on f.id = pod.facilityid  " +
                        " JOIN vw_districts d ON d.district_ID = F.GEOGRAPHICZONEID " +
                        " join REQUISITIONS R on R.ID = POD.ORDERID" +
                        " JOIN requisition_line_items item on item.rnrid = R.ID and item.productcode = li.productcode" +
                        " where li.QUANTITYRECEIVED > 0 AND R.periodid = '"+filter.getPeriod()+"' and POD.facilityId = '"+filter.getFacility()+"'  " +
                        " and POD.programId = '"+filter.getProgram()+"' AND R.STATUS IN('RELEASED') and item.quantityApproved > 0 " ;
    }




    public static String getQueryReport(Map params) {

        ItemFillRateReportParam filter = (ItemFillRateReportParam) params.get("filterCriteria");

        return "          SELECT l.productcode msdproductcode, CASE WHEN  (m.productCode is null) then l.productCode else m.productCode  end as eLMISProductCode ,m.product, coalesce(quantityApproved,0) quantityApproved,coalesce(quantityShipped,0) quantityShipped \n" +
                "                        FROM (\n" +
                "                       WITH Q AS (  SELECT p.packsize * quantityshipped as quantityshipped ,productcode, orderId from  POD\n" +

                "                         JOIN pod_line_items item on pod.id = item.podId\n" +
                "                         Join products p ON p.code = item.productcode\n" +
                "                         where pod.orderId in (select r.id from requisitions r JOIN \n" +
                "                         processing_periods pp on pp.id = r.periodID where programId = 1 and pp.id = 93 )\n" +

                "                        ) \n" +
                "                        SELECT  productcode, SUM(quantityshipped) quantityshipped FROM Q\n" +
                "                        GROUP BY productcode\n" +
                "                        \n" +
                "                        )L FULL OUTER JOIN \n" +
                "                        \n" +
                "                    \n" +
                "                       (\n" +
                "                       \n" +
                "                        WITH Q AS ( SELECT ITEM.productcode, item.product, sum(quantityApproved) quantityApproved\n" +
                "                         \n" +
                "                        \n" +
                "                         FROM REQUISITIONS R \n" +
                "\n" +
                "                         JOIN requisition_line_items item on item.rnrid = R.ID \n" +
                "                         \n" +
                "                     \n" +
                "                         WHERE  SKIPPED = false and status ='RELEASED' and  programId = 1 and periodid = 93 \n" +
                "                         \n" +
                "                         GROUP BY ITEM.productcode, item.product ) \n" +
                "                         \n" +
                "                         SELECT * FROM q\n" +
                "                       \n" +
                "                       ) M on M.productcode = l.productcode\n" +
                "                        \n" +
                "                        order by msdproductcode, eLMISProductCode ";



    }

}
