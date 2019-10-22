package org.openlmis.report.builder;

import org.openlmis.report.model.params.DistrictFundUtilizationParam;
import org.openlmis.report.model.params.DistrictSummaryReportParam;

import java.util.Map;

public class DistrictFundUtilizationStatusQueryBuilder {


    public static String getQuery(Map params) {

        DistrictFundUtilizationParam filterCriteria = (DistrictFundUtilizationParam) params.get("filterCriteria");
        Long userId = (Long) params.get("userId");


        return " WITH Q as (  SELECT * FROM crosstab ( \n" +
                "            $$SELECT fa.id::BIGINT, f.name, sum(quantity)  quantity\n" +
                "            \n" +
                "             FROM requisition_source_of_funds f\n" +
                "            \n" +
                "            JOIN requisitions r on f.rnrid = r.id " +
                "            JOIN processing_periods pd ON r.periodId = pd.id  \n" +
                "            \n" +
                "            JOIN facilities FA ON r.facilityId = fa.id\n" +
                "            JOIN vw_districts d ON fA.geographiczoneId  = d.district_id " +

                "         \n" +
                          //  writePredicates(filterCriteria, userId) +
                "            group by f.name,fa.id\n" +
                "            order by 1,2 $$\n" +
                "            ) AS (id BIGINT, other BIGINT, userFees BIGINT \n" +
                "            \n" +
                "            ) )\n" +
                "\n" +
                "SELECT * , ft.name facilityType,f.code facilityCode FROM Q\n" +
                "JOIN facilities f ON f.id = Q.id\n" +
                "JOIN vw_districts d ON f.geographiczoneId  = d.district_id " +
                " JOIN facility_types ft ON f.typeId = FT.ID ";

    }

    private static String writePredicates(DistrictFundUtilizationParam filter, Long userId) {
        String predicate = "";

        if (filter != null) {

            predicate = "where periodId =  " + filter.getPeriod() + " and ";
            predicate = predicate + " r.facilityId in (select facility_id from vw_user_facilities where user_id = " + userId + " and program_id = " + filter.getProgram() + ")";
            predicate = predicate + " and status in ('IN_APPROVAL','APPROVED','RELEASED') ";

            if (filter.getZone() != 0) {
                predicate = predicate + " and ( zone_id = " + filter.getZone() + " or parent = " + filter.getZone() + " or region_id = " + filter.getZone() + " or district_id = " + filter.getZone() + ") ";
            }

            if (filter.getSchedule() != 0) {
                predicate = predicate.isEmpty() ? " where " : predicate + " and ";
                predicate = predicate + " scheduleId= " + filter.getSchedule();
            }

            if (filter.getProgram() != 0) {
                predicate = predicate.isEmpty() ? " where " : predicate + " and ";
                predicate = predicate + " programId = " + filter.getProgram();
            }
        }
        return predicate;
    }


}
