package org.openlmis.analytics.Builder;

import java.util.Map;

public class TimelinessReportingQueryBuilder {

    public static String getQuery(Map params){

        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");
        Long userId = (Long)params.get("userId");


        return " \n" +
                "  SELECT totalExpected,reportedOnTime, reportedLate, unscheduled,NOT_REPORTED" +
  /*              "" +
                "" +
                ",case when totalExpected > 0 then \n" +
                "\t\t  round(reportedOnTime/totalExpected * 100,0) end as reportedOnTime,\n" +
                "\t\t case when totalExpected > 0 then round(reportedLate/totalExpected * 100,0) end as reportedLate,\n" +
                "\t\t case when totalExpected > 0 then round(unscheduled/totalExpected * 100,0) end as unscheduled,\n" +
                "\t\t case when totalExpected > 0 then round(NOT_REPORTED/totalExpected * 100,0) end as NOT_REPORTED\n" +*/
                "\n" +
                "              FROM (\n" +
                "                \n" +
                "               SELECT SUM(totalExpected) totalExpected, \n" +
                "               SUM(reportedOnTime) reportedOnTime,\n" +
                "               SUM(reportedLate) reportedLate,\n" +
                "               SUM(unscheduled) unscheduled,\n" +
                "               SUM(totalExpected) -  (SUM(reportedOnTime)+  SUM(reportedLate) + SUM(unscheduled)) NOT_REPORTED \n" +
                "               FROM (\n" +
                "\n" +
                "               SELECT * ,COALESCE(expected.COUNT,0) totalExpected FROM     \n" +
                "               ( SELECT Y.geographicZoneId, SUM(CASE WHEN REPORTING = 'R' then 1 else 0 end ) as reportedOnTime,  \n" +
                "\t\t       SUM(CASE WHEN REPORTING = 'L' then 1 else 0 end) as reportedLate,\n" +
                "\t\t       SUM(CASE WHEN REPORTING = 'U' then 1 else 0 end) as unscheduled\n" +
                "                 FROM ( \n" +
                "                SELECT * , reportingstatus reporting\n" +
                "                from mv_dashboard_timeliness_report r where programid = '"+program+"'::int AND PERIODID = '"+period+"'::int \n" +
                "\t         \t\t\n" +
                "                  )Y\n" +
                "                  Group by geographicZoneId ) M\n" +
                "\n" +
                "                  \n" +
                "                   LEFT JOIN   \n" +
                "                       (\n" +
                "                       select geographicZoneId, count(*)  from VW_EXPECTED_FACILITIES    \n" +
                "                       where programId = '"+program+"'::int AND PERIODID = '"+period+"'::int\n" +
                "                       group by geographicZoneId   \n" +
                "                       ) expected on M.geographiczoneid = expected.geographiczoneId  \n" +
                "               )L\n" +
                "               )LL\n";


    }


}
