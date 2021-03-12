package org.openlmis.analytics.Builder;

import java.util.Map;

public class DashboardTLEConsumptionTrend {

    public static String getQuery(Map params){

        Long year =(Long)params.get("year");

        return " SELECT periodName,scheduleid schedule, sum(tle_consumption) tle_consumption, sum(tld_consumption) tld_consumption, sum(Dolutegravir_consumption) Dolutegravir_consumption FROM (\n" +
                "\n" +
                "SELECT PERIODID,scheduleid, periodName, CASE WHEN PRODUCTCODE ='10010164AB'  THEN amc ELSE 0 end as tle_consumption,\n" +
                "\n" +
                " CASE WHEN PRODUCTCODE ='10010022AB' THEN amc else 0 end as tld_consumption, \n" +
                " CASE WHEN PRODUCTCODE ='10010021AB' THEN amc else 0 end as Dolutegravir_consumption\n" +
                "\n" +
                " FROM mv_dashboard_TLE_consumption_trends WHERE REPORTYEAR = '"+year+"' \n" +
                "\n" +
                " )X\n" +
                "\n" +
                "group by periodName,periodid,scheduleid\n" +
                "order by scheduleid,periodId\n";

    }



}
