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
                        " join REQUISITIONS R on R.ID = POD.ORDERID " +
                        " JOIN requisition_line_items item on item.rnrid = R.ID " +
                        " where li.QUANTITYRECEIVED > 0 AND R.periodid = '"+filter.getPeriod()+"' and POD.facilityId = '"+filter.getFacility()+"'  " +
                        " and POD.programId = '"+filter.getProgram()+"' AND R.STATUS IN('RELEASED') and item.quantityApproved > 0 " ;
    }

}
