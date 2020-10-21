package org.openlmis.report.builder;


import org.openlmis.report.model.params.EmergencyRnrAggregateByGeozoneReportParam;

import java.util.Map;

import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;


public class EmergencyRnrAggregateByGeozoneQueryBuilder extends ConsumptionBuilder {

    public static String getQuery(Map params) {
        EmergencyRnrAggregateByGeozoneReportParam filter = (EmergencyRnrAggregateByGeozoneReportParam) params.get("filterCriteria");
        String query = "select z.district_name as geograhicZone , z.district_id as geograhicZoneId," +
                " p.name as programArea, p.id,  pp.name as period, pp.id as periodId, count(*) reported \n" +
                "from requisitions r\n" +
                " join facilities f on r.facilityid = f.id\n" +
                " join vw_districts z on f.geographiczoneid = z.district_id\n" +
                " join programs p on r.programid = p.id\n" +
                " join processing_periods pp on pp.id = r.periodid\n" +
                "where emergency = true\n" +
                writePredicates(filter) +
                "group by z.district_name, z.district_id, p.name, p.id,  pp.name, pp.id \n" +
                "order by z.district_name";

        query = query +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;
    }

    public static String getTotalCountQuery(Map params) {
        EmergencyRnrAggregateByGeozoneReportParam filter = (EmergencyRnrAggregateByGeozoneReportParam) params.get("filterCriteria");
        String query = "select z.name as geograhicZone , z.id, p.name as programArea, p.id,  pp.name as period, pp.id, count(*) reported \n" +
                "from requisitions r \n" +
                "join facilities f on r.facilityid = f.id  \n" +
                "join geographic_zones z on f.geographiczoneid = z.id\n" +
                "join programs p on r.programid = p.id\n" +
                "join processing_periods pp on pp.id = r.periodid\n" +
                "where emergency = true\n" +
                writePredicates(filter) +
                "group by z.name, z.id, p.name, p.id,  pp.name, pp.id \n" +
                "order by z.name\n" +
                "limit 300";

        query = query + "select count(*) from (" + query + ")";

        return query;

    }

    private static String writePredicates(EmergencyRnrAggregateByGeozoneReportParam filter) {
        String predicate = " and " + programIsFilteredBy("r.programid") +
                " and " + startDateFilteredBy("pp.startdate::date", filter.getPeriodStart()) +
                " and " + endDateFilteredBy("pp.enddate::date", filter.getPeriodEnd());

        if (filter.getZone() != 0) {
            predicate = predicate + "and " + geoZoneIsFilteredBy("z");
        }

        return predicate;
    }
}
