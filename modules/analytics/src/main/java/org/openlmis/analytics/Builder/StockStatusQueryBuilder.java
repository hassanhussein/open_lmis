package org.openlmis.analytics.Builder;

import java.util.Map;

public class StockStatusQueryBuilder {

    public static String getStockStatusSummary(Map params){

         Long userId = (Long)params.get("userId");
         Long product = (Long)params.get("product");
         Long program = (Long)params.get("program");
         Long year = (Long)params.get("year");


        return "               SELECT periodId, periodName, SUM (SO) as so, \n" +
                "              SUM(OS) AS OS , SUM( SP) SP , SUM(US) US ,\n" +
                "              SUM(UK) UK FROM (\n" +
                "                SELECT periodid,periodName,\n" +
                "                case when status = 'SO' THEN 1 ELSE 0 END AS SO,\n" +
                "                case when status = 'SP' THEN 1 ELSE 0 END AS SP,\n" +
                "                case when status = 'OS' THEN 1 ELSE 0 END AS OS,\n" +
                "\n" +
                "                case when status = 'US' THEN 1 ELSE 0 END AS US,\n" +
                "                case when status = 'UK' THEN 1 ELSE 0 END AS UK\n" +
                "                \n" +
                "                from mv_dashboard_tracer_availability_summary\n" +
                "\n" +
                "                 WHERE productId = '"+product+"'::int and programId = '"+program+"'::INT  AND reportedYEAR = '"+year+"'\n" +
                "                \n" +
                "                )L\n" +
                "                GROUP BY  periodid,periodName\n" +
                "                ORDER BY periodid,periodName  ";

    }


}
