package org.openlmis.analytics.Builder;

import java.util.Map;

public class DashboardConsumptionSummaryQueryBuilder {

 public static String getSummary(Map params) {
     Long year = (Long)params.get("year");
     String productIds = (String) params.get("product");
     Long userId = (Long)params.get("userId");
     Long program = (Long)params.get("program");

     Long schedule = (Long)params.get("schedule");

     return " select productCode,productname, periodName, sum(amc)amc,sum(stockinhand) soh, sum(mos) mos from \n" +
             "\n" +
             "mv_dashboard_consumption_summary where " +
             "  scheduleId = '"+schedule+"'::int and year = '"+year+"'::INT and programId = '"+program+"'::int and productId IN('"+productIds+"'::INT)\n" +

             "GROUP BY periodId,PERIODNAME,productCode,productname" +
             " ORDER BY periodID ";
 }




}
