package org.openlmis.analytics.Builder;

import java.util.Map;

public class DashboardIndexOfStockAvailabilityQueryBuilder {

    public static String getQuery(Map params) {

        Long userId = (Long)params.get("userId");
        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");


        return " SELECT \n" +
                "*\n" +
                "\n" +
                "\n" +
                "FROM (\n" +
                "\n" +
                "SELECT status,\n" +
                "\n" +
                "SUM(one_available::NUMERIC) +\n" +
                "SUM(two_available::NUMERIC) +\n" +
                "SUM(third_available::NUMERIC) +\n" +
                "SUM(fourth_available::NUMERIC) +\n" +
                "SUM(all_stockedout::NUMERIC) as total\n" +
                "\n" +
                "\n" +
                "FROM(\n" +
                "\n" +
                "WITH Q AS (SELECT * FROM (\n" +
                "\n" +
                "SELECT \n" +
                "COUNT(rnrid) total,productcode,\n" +
                "\n" +
                "FACILITYid,periodID, MAX(case when l.stockinHand > 0 then 1 else 0 end ) available, \n" +
                " MAX(case when l.stockinHand = 0 then 1 else 0 end ) notAvailable, \n" +
                "CASE WHEN( MAX(case when l.stockinHand = 0 then 1 else 0 end ) ) = 1 then 0 ELSE 1 end as fullStock,\n" +
                " MAX(l.stockinHand) soh2\n" +
                "from requisitions r \n" +
                "join requisition_line_items l on r.id = l.rnrid\n" +
                "JOIN products p ON p.code::text = l.productcode::text\n" +
                "join program_products pp ON pp.productId =p.id\n" +
                "join processing_periods pr on pr.id = r.periodid\n" +
                "where \n" +
                " --productid = 2413::INT\n" +
                " status in('APPROVED','RELASED') AND periodid=95 and R.programId= 1 and l.productcode in('10010170BE','10010002BE','10010171BE','10010169BE') AND emergency = FALSE\n" +
                " \n" +
                " GROUP BY FACILITYID,periodID,productcode\n" +
                " ORDER BY FACILITYID\n" +
                ")X\n" +
                "\n" +
                ")\n" +
                "\n" +
                "SELECT \n" +
                "\n" +
                "FACILITYID,sum(total) totalProduct, \n" +
                "Case when sum(available)=1 then 1 ELSE 0 end as one_available ,  \n" +
                "Case when sum(available)=2 then 1 ELSE 0 end as two_available , \n" +
                "Case when sum(available)=3 then 1 ELSE 0 end as third_available ,\n" +
                "Case when sum(available)=4 then 1 ELSE 0 end as fourth_available ,\n" +
                "Case WHEN sum(available)=0 then 1 ELSE 0 END AS all_stockedout, \n" +
                "\n" +
                "Case\n" +
                "when sum(available)=1 then 'Facilities with 1 Presentation' \n" +
                " when  sum(available)=2 then 'Facilities with 2 Presentation'\n" +
                " when sum(available)=3 then 'Facilities with 3 Presentation '\n" +
                " when sum(available)= 4 then 'Facilities with 4 Presentation '\n" +
                " WHEN sum(available)=0 then 'Facilities with 0 Presentation' ELSE 'unknown' end as status\n" +
                " \n" +
                "from Q\n" +
                "GROUP by FACILITYID\n" +
                "\n" +
                ")y\n" +
                "\n" +
                "GROUP by status\n" +
                ")z\n ";

    }


}
