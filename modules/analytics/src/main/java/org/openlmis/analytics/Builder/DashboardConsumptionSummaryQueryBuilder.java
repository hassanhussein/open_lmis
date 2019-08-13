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
             "mv_dashboard_consumption_summary where programId = '"+program+"'::int and productId IN('"+productIds+"'::INT)\n" +

             "GROUP BY PERIODNAME,productCode,productname ";
 }




}
