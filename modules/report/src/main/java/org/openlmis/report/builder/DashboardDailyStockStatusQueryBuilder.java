package org.openlmis.report.builder;

public class DashboardDailyStockStatusQueryBuilder {

    public static String getQuery(){
        String query="WITH daily_stock_status_by_program AS\n" +
                "(\n" +
                "  SELECT f.id,\n" +
                "         f.code,\n" +
                "         f.name,\n" +
                "         p.code AS programcode,\n" +
                "         SUM(COUNT) AS countofsubmissions\n" +
                "  FROM (SELECT facilityid,\n" +
                "               programid,\n" +
                "               DATE::DATE AS DATE,\n" +
                "               1 AS COUNT\n" +
                "        FROM daily_stock_status dss\n" +
                "          INNER JOIN daily_stock_status_line_items dssli ON dss.id = dssli.stockstatussubmissionid\n" +
                "\t\twhere dss.DATE >= (now() -'700 days'::interval)::date\n" +
                "        GROUP BY facilityid,\n" +
                "                 programid,\n" +
                "                 DATE) t1\n" +
                "    JOIN facilities f ON t1.facilityid = f.id\n" +
                "    JOIN programs p ON p.id = t1.programid\n" +
                "  GROUP BY f.id,\n" +
                "           f.name,\n" +
                "           p.id\n" +
                ")\n" +
                "SELECT dssbp.name,\n" +
                "       dssbp.programcode,\n" +
                "       dssbp.countofsubmissions\n" +
                "FROM daily_stock_status_by_program dssbp\n" +
                "order by dssbp.name, programcode\n";
        return query;
    }
}
