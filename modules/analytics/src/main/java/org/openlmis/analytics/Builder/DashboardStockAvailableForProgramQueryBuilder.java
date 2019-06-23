package org.openlmis.analytics.Builder;


import java.util.Map;

public class DashboardStockAvailableForProgramQueryBuilder {

    public static String getQuery (Map params){

    Long userId = (Long)params.get("userId");
    Long period = (Long)params.get("period");

        return
                "WITH Q AS (SELECT programid, PROGRAM_name, COUNT(*) totalByProgram FROM \n" +
                        "\n" +
                        "mv_dashboard_tracer_available_by_programs\n" +
                        "WHERE periodid = " +period+
                        "GROUP BY PROGRAM_name,programid\n" +
                        "\n" +
                        ")\n" +
                        "SELECT * FROM Q \n" +
                        "JOIN mv_dashboard_tracer_product_by_program_counts c ON c.programId = Q.programId\n" +
                        "ORDER BY PROGRAM_name ";

    }


    public static String getStockForProductandProgram (Map params){
        Long program = (Long)params.get("program");
        Long period = (Long)params.get("period");

        return
                "select productName, sum(amc) amc, CASE\n" +
                        "            WHEN COALESCE(SUM(amc), 0) = 0 THEN 0::numeric\n" +
                        "            ELSE trunc(round(SUM(stockinhand)::numeric / SUM(amc)::numeric, 2), 1)\n" +
                        "        END AS mos ," +
                        " sum(stockinhand) soh  from mv_dashboard_tracer_reported_products_by_programs\n" +
                "where programId ='"+program+"' AND PERIODID = '"+period+"'\n" +
                "group by productName\n" +
                "order by mos desc ";

    }



}
