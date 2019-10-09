package org.openlmis.report.builder;

import java.util.Map;

public class DistrictFundUtilizationStatusQueryBuilder {


    public static String getQuery(Map params) {

        Map filterCriteria = (Map) params.get("filterCriteria");
        Long userId = (Long) params.get("userId");


        return " WITH Q as (  SELECT * FROM crosstab ( \n" +
                "            $$SELECT fa.id::BIGINT, f.name, sum(quantity)  quantity\n" +
                "            \n" +
                "             FROM requisition_source_of_funds f\n" +
                "            \n" +
                "            JOIN requisitions r on f.rnrid = r.id \n" +
                "            \n" +
                "            JOIN facilities FA ON r.facilityId = fa.id\n" +
                "         \n" +
                "            group by f.name,fa.id\n" +
                "            order by 1,2 $$\n" +
                "            ) AS (id BIGINT, other BIGINT, userFees BIGINT \n" +
                "            \n" +
                "            ) )\n" +
                "\n" +
                "SELECT * FROM Q\n" +
                "JOIN facilities f ON f.id = Q.id\n" +
                "JOIN vw_districts d ON f.geographiczoneid  = d.district_id ";

    }

    private static String writePredicates(Map params, Long userId) {

        String predicate = "  WHERE ";
        String period = params.get("period") == null ? null : ((String[]) params.get("period"))[0];
        String program = params.get("program") == null ? null : ((String[]) params.get("program"))[0];

        String zone = params.get("zone") == null ? null : ((String[]) params.get("zone"))[0];

        String schedule = params.get("schedule") == null ? null : ((String[]) params.get("schedule"))[0];
        predicate += "  facilityId in (select facility_id from vw_user_facilities where user_id = " + userId + " and program_id = " + program + ")";
        predicate += " and periodId = " + period;

        predicate += " and programId = " + program;

        predicate += " and scheduleId = " + schedule;


        if (zone != null && !zone.isEmpty() && !zone.equals("0") && !zone.equals("-1")) {

            predicate += " and (district_id = " + zone + " or zone_id =  " + zone + "  or region_id =  " + zone + "  or parent =  " + zone + " ) ";
        }

        return predicate;
    }

}
