package org.openlmis.report.builder;

import org.openlmis.report.model.params.DistrictFundUtilizationParam;

import java.util.Map;

public class DistrictFundUtilizationStatusQueryBuilder {


    public static String getQuery(Map params) {

        DistrictFundUtilizationParam filterCriteria = (DistrictFundUtilizationParam) params.get("filterCriteria");
        Long userId = (Long) params.get("userId");


        return " \n" +
                "                           SELECT fa.id::BIGINT,fa.name facilityName, f.name sourceofFundName, sum(quantity)  quantity,  ft.name facilityType,fa.code facilityCode,district_name, region_name, zone_name\n" +
                "                            \n" +
                "                             FROM requisition_source_of_funds f\n" +
                "                            \n" +
                "                            JOIN requisitions r on f.rnrid = r.id  \n" +
                "                            JOIN processing_periods pd ON r.periodId = pd.id  \n" +
                "                            \n" +
                "                            JOIN facilities FA ON r.facilityId = fa.id\n" +
                "                            JOIN vw_districts d ON fA.geographicZoneId  = d.district_id  \n" +
                "                            JOIN facility_types ft ON fa.typeId = FT.ID\n" +
                writePredicates(filterCriteria, userId)+
                "                            \n" +
                "                            group by f.name,fa.name  ,fa.id,district_name, region_name, zone_name,fa.code,ft.name\n" +
                "                            order by 1,2\n";

    }

    private static String writePredicates(DistrictFundUtilizationParam filter, Long userId) {
        String predicate = "";

        if (filter != null) {

           // predicate = "where periodId =  " + filter.getPeriod() + " and ";
            predicate = predicate + " where  r.facilityId in (select facility_id from vw_user_facilities where user_id = " + userId + " and program_id = " + filter.getProgram() + ")";
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
