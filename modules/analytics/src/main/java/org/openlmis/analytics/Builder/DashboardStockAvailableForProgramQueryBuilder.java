package org.openlmis.analytics.Builder;


import java.util.Map;

public class DashboardStockAvailableForProgramQueryBuilder {

    public static String getQuery (Map params){

    Long userId = (Long)params.get("userId");
    Long period = (Long)params.get("period");

        return
                "WITH Q AS (SELECT programid, PROGRAM_name, COUNT(*) TOTAL FROM \n" +
                "mv_dashboard_tracer_available_by_programs\n" +
                "WHERE periodid = " +period +
                "GROUP BY PROGRAM_name,programid\n" +
                ")\n" +
                "SELECT * FROM Q \n" +
                "JOIN mv_dashboard_tracer_product_by_program_counts c ON c.programId = Q.programId\n " +
                        " ORDER BY PROGRAM_name";

    }



}
