package org.openlmis.analytics.Builder;

import java.util.Map;

public class DashboardConsumptionSummaryQueryBuilder {

 public static String getSummary(Map params) {
     Long period = (Long)params.get("period");
     String productIds = (String) params.get("product");
     Long userId = (Long)params.get("userId");
     Long program = (Long)params.get("program");

     return " select productCode,productname, periodName, sum(amc)amc,sum(stockinhand) soh, sum(mos) mos from \n" +
             "\n" +
             "mv_dashboard_consumption_summary where periodId ='"+period+"'::int and programId = '"+program+"'::int and productId = '1272'::INT\n" +

             "GROUP BY PERIODNAME,productCode,productname ";
 }




}
