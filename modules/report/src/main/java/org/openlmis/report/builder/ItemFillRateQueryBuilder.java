package org.openlmis.report.builder;

import org.openlmis.report.model.params.DistrictFundUtilizationParam;
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

        String sql =  "                              SELECT l.productcode msdProductCode, CASE WHEN  (m.productCode is null) then l.productCode else m.productCode  end as eLMISProductCode ,m.product, coalesce(quantityApproved,0) quantityApproved,coalesce(quantityShipped,0) quantityShipped  \n" +
                "\n" +
                "                                        FROM ( \n" +
                "                                        \n" +
                "                                       WITH Q AS (  SELECT p.packsize * quantityshipped as quantityshipped ,productcode, orderId from  POD \n" +
                "\n" +
                "                                         JOIN pod_line_items item on pod.id = item.podId \n" +
                "                                         Join products p ON p.code = item.productcode \n" +
                "                                         where pod.orderId in (select r.id from requisitions r JOIN  \n" +
                "                                         processing_periods pp on pp.id = r.periodID where programId ='"+filter.getProgram()+"' and pp.startdate >='"+filter.getPeriodStart()+"' and pp.enddate <='" +filter.getPeriodEnd()+"') \n" +
                "\n" +
                "                                        )  \n" +
                "                                        SELECT  productcode, SUM(quantityshipped) quantityshipped FROM Q \n" +
                "                                        GROUP BY productcode \n" +
                "                                         \n" +
                "                                        )L FULL OUTER JOIN  \n" +
                "                                         \n" +
                "                                       ( \n" +
                "                                        \n" +
                "                                        WITH Q AS ( SELECT ITEM.productcode, item.product, sum(quantityApproved) quantityApproved \n" +
                "                                          \n" +
                "                                         \n" +
                "                                         FROM REQUISITIONS R  \n" +
                "                 \n" +
                "                                         JOIN requisition_line_items item on item.rnrid = R.ID  \n" +
                "                                         JOIN processing_periods pp ON pp.id = R.periodID" +
                "                                          \n" +
                "                                      \n" +
                "                                         WHERE  SKIPPED = false and status ='RELEASED' and  programId = '"+filter.getProgram()+"' and pp.startdate >='"+filter.getPeriodStart()+"' and pp.enddate <= '" +filter.getPeriodEnd()+"' \n" +
                "                                          \n" +
                "                                         GROUP BY ITEM.productcode, item.product )  \n" +
                "                                          \n" +
                "                                         SELECT * FROM q \n" +
                "                                        \n" +
                "                                       ) M on M.productcode = l.productcode \n" +
                "                                         \n" +
                "                                        order by msdproductcode, eLMISProductCode  ";

        return sql;

    }


    public static String getItemByRnr(Map params) {

        ItemFillRateReportParam filter = (ItemFillRateReportParam) params.get("filterCriteria");

       return "   SELECT l.productcode msdProductCode, CASE WHEN  (m.productCode is null) then l.productCode else m.productCode  end as eLMISProductCode ,m.product, coalesce(quantityApproved,0) quantityApproved,coalesce(quantityShipped,0) quantityShipped  \n" +
               "                \n" +
               "   FROM ( \n" +
               "                                                       \n" +
               "   WITH Q AS (  SELECT p.packsize * quantityshipped as quantityshipped ,productcode, orderId, InvoiceDate from  POD \n" +
               "                \n" +
               "   JOIN pod_line_items item on pod.id = item.podId \n" +
               "   Join products p ON p.code = item.productcode \n" +
               "   where pod.orderId in (select r.id from requisitions r JOIN  \n" +
               "   processing_periods pp on pp.id = r.periodID where programId ='"+filter.getProgram()+"' and pp.id = '"+filter.getPeriod()+"' and r.id = '"+filter.getOrderId()+"') \n" +
               "                \n" +
               "   )  \n" +
               "   SELECT  productcode, SUM(quantityshipped) quantityshipped FROM Q \n" +
               "\n" +
               "   GROUP BY productcode \n" +
               "                                                        \n" +
               "  )L FULL OUTER JOIN  \n" +
               "                                                        \n" +
               "  ( \n" +
               "                                   \n" +
               " WITH Q AS ( SELECT ITEM.productcode, item.product, sum(quantityApproved) quantityApproved \n" +
               "                                                         \n" +
               " FROM REQUISITIONS R  \n" +
               "                              \n" +
               " JOIN requisition_line_items item on item.rnrid = R.ID  \n" +
               "                                                         \n" +
               " WHERE  SKIPPED = false and status ='RELEASED' and  programId = '"+filter.getProgram()+"' and periodId = '"+filter.getPeriod()+"' and r.id = '"+filter.getOrderId()+"'\n" +
               "                                                         \n" +
               " GROUP BY ITEM.productcode, item.product )  \n" +
               "                                                         \n" +
               " SELECT * FROM q \n" +
               "                                                       \n" +
               ") M on M.productcode = l.productcode \n" +
               " order by msdproductcode, eLMISProductCode " ;

    }

    public static String getItemFillRateByFacility(Map params) {

        ItemFillRateReportParam  filter = (ItemFillRateReportParam) params.get("filterCriteria");

        Long userId = (Long) params.get("userId");

       String sql =  " \n" +
               "\n" +
               "                                     WITH Q AS (\n" +
               "                                      SELECT facilityId, periodID, requisitions.ID, EMERGENCY,\n" +
               "                                      (select count(*) totalItems from requisition_line_items where rnrId = requisitions.id and SKIPPED = false),\n" +
               "\n" +
               "                                      (SELECT count(*) totalReceived from  POD \n" +
               "\n" +
               "                                       JOIN pod_line_items item on pod.id = item.podId \n" +
               "                                         where orderId = requisitions.id\n" +
               "                                       )\n" +
               "                                      from requisitions \n" +
               " join processing_periods pp on pp.id=requisitions.periodID " +
               "                                      \n" +
               "                                      WHERE  pp.startdate >='"+filter.getPeriodStart()+"' and pp.enddate <='" +filter.getPeriodEnd() + "' and programID = '"+filter.getProgram()+"' AND STATUS = 'RELEASED' \n" +
               "                                      \n" +
               "                                      GROUP BY facilityid, periodid, EMERGENCY,requisitions.ID\n" +
               "\n" +
               "                                      order by facilityId, requisitions.Id\n" +
               "                                      ) select f.name facilityName, district_name district,region_name region, zone_name msdZONE, orderNumber, emergency, q.id rnrId, \n" +
               "                                        totalItems  approvedQuantity, totalReceived receivedQuantity, invoiceNumber, invoiceDate,p.name periodName from q \n" +
               "                                      JOIN facilities f on q.facilityId = F.ID\n" +
               "                                      join vw_districts d on d.district_id = f.geographiczoneid\n" +
               "                                      JOIN processing_periods p on q.periodId = p.id\n" +
               "                                      Join pod on q.id = pod.orderid\n" +
                                                      writePredicates(filter, userId)+
               "                                      and  totalreceived > 0 " +
               "" +
               " order by f.id ";

return sql;
    }



    private static String writePredicates(ItemFillRateReportParam filter, Long userId) {
        String predicate = "";

        if (filter != null) {

            // predicate = "where periodId =  " + filter.getPeriod() + " and ";
            predicate = predicate + " where  f.id in (select facility_id from vw_user_facilities where user_id = " + userId + " and program_id = " + filter.getProgram() + ")";
            // predicate = predicate + " and status in ('IN_APPROVAL','APPROVED','RELEASED') ";

            if (filter.getZone() != 0) {
                predicate = predicate + " and ( zone_id = " + filter.getZone() + " or parent = " + filter.getZone() + " or region_id = " + filter.getZone() + " or district_id = " + filter.getZone() + ") ";
            }

         /*   if (filter.getSchedule() != 0) {
                predicate = predicate.isEmpty() ? " where " : predicate + " and ";
                predicate = predicate + " scheduleId= " + filter.getSchedule();
            }*/

         /*   if (filter.getProgram() != 0) {
                predicate = predicate.isEmpty() ? " where " : predicate + " and ";
                predicate = predicate + " programId = " + filter.getProgram();
            }*/
        }
        return predicate;
    }


}
