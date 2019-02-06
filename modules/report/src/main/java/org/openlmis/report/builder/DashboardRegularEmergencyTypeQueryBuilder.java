package org.openlmis.report.builder;

public class DashboardRegularEmergencyTypeQueryBuilder {

    public static String getQuery(){
        String query="with period as\n" +
                "( select pp.id,pp.name,pp.startdate from processing_periods pp   \n" +
                "  inner join processing_schedules ps on pp.scheduleid=ps.id   \n" +
                " where lower(ps.name)= 'monthly'  and pp.enddate < current_date\n" +
                " order by pp.startdate desc limit 3), \n" +
                " type_count as( select p.id period_id, p.name period,\n" +
                "\t\t\t p.startdate, count(*) FILTER (WHERE emergency = true) " +
                "  AS emergency,  count(*) FILTER (WHERE emergency = false) AS regular, " +
                "  count(*) total from requisitions r  inner join period p on r.periodid=p.id  group by 1,2,3  ) \n" +
                "\t\t\t select tc.period_id,tc.period,tc.startdate, tc.regular, tc.emergency, tc.total,\n" +
                "\t\t\t case when COALESCE(tc.total,0)>0  \n" +
                "then round(100*(COALESCE(tc.emergency,0)::Numeric/COALESCE(tc.total,0)),2)  else 0 end percent\n" +
                "from type_count tc \n" +
                "order by startdate";
        return query;
    }

}
