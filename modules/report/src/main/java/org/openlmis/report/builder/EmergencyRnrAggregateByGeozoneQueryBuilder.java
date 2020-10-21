package org.openlmis.report.builder;


import org.openlmis.report.model.params.EmergencyRnrAggregateByGeozoneReportParam;

import java.util.Map;


public class EmergencyRnrAggregateByGeozoneQueryBuilder extends ConsumptionBuilder {

    public static String getQuery(Map params) {
        EmergencyRnrAggregateByGeozoneReportParam filter = (EmergencyRnrAggregateByGeozoneReportParam) params.get("filterCriteria");
        String query = "select z.name as geograhicZone , z.id, p.name as programArea, p.id,  pp.name as period, pp.id, count(*) reported \n" +
                "from requisitions r \n" +
                "join facilities f on r.facilityid = f.id  \n" +
                "join geographic_zones z on f.geographiczoneid = z.id\n" +
                "join programs p on r.programid = p.id\n" +
                "join processing_periods pp on pp.id = r.periodid\n" +
                "where emergency = true\n" +
                "group by z.name, z.id, p.name, p.id,  pp.name, pp.id \n" +
                "order by z.name\n";

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
                "group by z.name, z.id, p.name, p.id,  pp.name, pp.id \n" +
                "order by z.name\n" +
                "limit 300";

        query = query + "select count(*) from (" + query + ")";

        return query;


    }
}
