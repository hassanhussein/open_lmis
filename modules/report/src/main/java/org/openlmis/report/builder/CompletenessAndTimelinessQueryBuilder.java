/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.report.builder;

import org.openlmis.report.model.params.CompletenessAndTimelinessReportParam;

import java.util.Map;


public class CompletenessAndTimelinessQueryBuilder {


    public static String selectCompletenessAndTimelinessMainReportDataByDistrict(Map param) {
        CompletenessAndTimelinessReportParam params = getParamsValues(param);

        String sql = " ";

        sql ="with completeness_with_reporting_periods as (select    \n" +
                "                                                  a.region_name,    \n" +
                "                                                  a.district_name,   \n" +
                "                                                  a.priod_id,       \n" +
                "                                                  a.period_name,    \n" +
                "                                                  a.period_start_date,   \n" +
                "                                                  a.geographiczoneid,\n" +
                "                                                  a.reported,    \n" +
                "                                                  a.ontime,     \n" +
                "                                                  a.late                                       \n" +
                "                                                from (   \n" +
                "                                                     with temp as  \n" +
                "                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t (  select  pp.id priod_id, pp.name period_name,    \n" +
                "                                                           pp.startdate::date period_start_date,    \n" +
                "                                                           z.id geographiczoneid,    \n" +
                "                                                           z.name district,    \n" +
                "                                                           f.name facility_name,    \n" +
                "                                                           f.code facility_code,     \n" +
                "                                                           to_char(COALESCE(r.statussubmissioneddate::date,r.createddate::date) , 'DD Mon YYYY') reported_date,\n" +
                "                                                         CASE    \n" +
                "                                                            WHEN date_part('day'::text, COALESCE(r.statussubmissioneddate::date,r.createddate::date)  - pp.enddate::date::timestamp without time zone) <=   \n" +
                "                                                            COALESCE((( SELECT configuration_settings.value FROM configuration_settings     \n" +
                "                                                            wHERE  configuration_settings.key::text = 'LATE_REPORTING_DAYS'::text))::integer, 0)::double precision   \n" +
                "                                                            THEN 'T'::text                                   \n" +
                "                                                            WHEN COALESCE(date_part('day'::text, COALESCE(r.statussubmissioneddate::date,r.createddate::date)  - pp.enddate::date::timestamp without time zone), 0::double precision) >   \n" +
                "                                                            COALESCE((( SELECT configuration_settings.value FROM configuration_settings     \n" +
                "                                                            wHERE  configuration_settings.key::text = 'LATE_REPORTING_DAYS'::text))::integer, 0)::double precision   \n" +
                "                                                            THEN 'L'::text     \n" +
                "                                                            ELSE 'N'::text     \n" +
                "                                                         END AS reporting_status    \n" +
                "                                                                from programs_supported ps     \n" +
                "                                                                join vw_requisitions_submitted_status r on r.programid = ps.programid and r.facilityid = ps.facilityid  and status not in ('INITIATED', 'SUBMITTED', 'SKIPPED')   \n" +
                "                                                                join processing_periods pp on pp.id = r.periodid    \n" +
                "                                                                right join facilities f on f.id = ps.facilityId      \n" +
                "                                                                and pp.startdate::date >='" + params.getPeriodStart() + "'::date and pp.enddate::date <=   '" + params.getPeriodEnd() +"'::date \n" +
                "                                                                join geographic_zones z on z.id = f.geographicZoneId     \n" +
                "                                                       )      \n" +
                "                                                         select                                                 \n" +
                "                                                                vd.region_name,    \n" +
                "                                                                vd.district_name,  \n" +
                "                                                                priod_id,        \n" +
                "                                                                t.period_name,    \n" +
                "                                                                t.period_start_date,   \n" +
                "                                                                t.geographiczoneid, \n" +
                "                                                                sum(case when reporting_status IN ('T','L') then 1 else 0 end) reported,    \n" +
                "                                                                sum(case when reporting_status = 'T' then 1 else 0 end) ontime,     \n" +
                "                                                                sum(case when reporting_status = 'L' then 1 else 0 end) late   \n" +
                "                                                        from temp t    \n" +
                "                                                            join vw_districts vd on vd.district_id = t.geographiczoneid                 \n" +
                "                \n" +
                "                                                        where vd.district_id in (select district_id from vw_user_facilities where user_id =  '" + params.getUserId() + "'::int   and program_id =  " + params.getProgram() + "::int)   \n" +
                writeDistrictPredicate(params.getDistrict())+
                "                                                        group by 1, 2, 3, 4,5 ,6   \n" +
                "                                                   \n" +
                "                                                ) a  ),                                                   \n" +
                "                                                completness_with_nonreporting_periods as (  \n" +
                "                                                      select c.geographiczoneid, periods.*,  \n" +
                "                                                            (select count(*) from vw_facility_start_periods fsp  \n" +
                "                        join vw_user_facilities uf on uf.facility_id = fsp.facilityid and uf.program_id =  '" + params.getProgram() + "'::int and uf.user_id =  '" + params.getUserId() + "'::int    \n" +
                "                                                             where fsp.startdate <= periods.period_end_date  \n" +
                "                                                         and fsp.geographiczoneid = c.geographiczoneid ) expected  \n" +
                "                                                        from   \n" +
                "                                                         (  \n" +
                "                                                              select id, scheduleid,name period_name, startdate period_start_date, enddate period_end_date from processing_periods pp   \n" +
                "                                                                where pp.startdate::date >=  '" + params.getPeriodStart() + "'::date and pp.enddate::date <=  '" + params.getPeriodEnd() + "'::date \n" +
                "                                                              AND pp.numberofmonths = 1   \n" +
                "                                                          ) periods ,   \n" +
                "                                                          (  \n" +
                "                                                              select distinct geographiczoneid from   \n" +
                "                                                              completeness_with_reporting_periods c  \n" +
                "                                                          ) c  \n" +
                "                                                )  \n" +
                "                                                  \n" +
                "                                                 SELECT           \n" +
                "                                                    vd.region_name as regionName,    \n" +
                "                                                    vd.district_name as districtName,   \n" +
                "                                                    nonreporting.period_name as periodName,   \n" +
                "                                                    nonreporting.period_start_date as periodStartDate,   \n" +
                "                                                    nonreporting.geographiczoneid as geographicZoneId,  \n" +
                "                                                    nonreporting.expected,   \n" +
                "                                                    c.reported,    \n" +
                "                                                    c.ontime,     \n" +
                "                                                    c.late,   \n" +
                "                                                    case when nonreporting.expected > 0 then trunc((c.reported::numeric/nonreporting.expected::numeric)*100,2) else 0 end percentReported,   \n" +
                "                                                    case when nonreporting.expected > 0 then trunc((c.late::numeric/nonreporting.expected::numeric)*100,2) else 0 end percentLate,  \n" +
                "                                                    CASE WHEN c.geographiczoneid is null then 'NONREPORTING' else 'REPORTING' end as reportingStatus  \n" +
                "                                                FROM completness_with_nonreporting_periods nonreporting   \n" +
                "                                                    join geographic_zones z on z.id = nonreporting.geographiczoneid    \n" +
                "                                                    join vw_districts vd on vd.district_id = nonreporting.geographiczoneid  \n" +
                "                                                    left outer join completeness_with_reporting_periods c  On c.geographiczoneid = nonreporting.geographiczoneid       \n" +
                "                                                                AND nonreporting.id = c.priod_id  \n" +
                "                                                                order by 1,2,4; ";


        return sql;
    }

    public static CompletenessAndTimelinessReportParam getParamsValues(Map param) {
        return (CompletenessAndTimelinessReportParam) param.get("filterCriteria");
    }

    public static String selectCompletenessAndTimelinessSummaryReportDataByDistrict(Map param) {


        CompletenessAndTimelinessReportParam params = getParamsValues(param);

        String sql = "";


        sql = "\n" +
                "with completeness_with_reporting_periods as ( \n" +
                "                  select     \n" +
                "                  a.region_name,    \n" +
                "                  a.district_name,   \n" +
                "                  a.priod_id,       \n" +
                "                  a.period_name,    \n" +
                "                  a.period_start_date,   \n" +
                "                  a.geographiczoneid,     \n" +
                "                  a.reported,    \n" +
                "                  a.ontime,     \n" +
                "                  a.late            \n" +
                "                  from (   \n" +
                "                       with temp as (  select  pp.id priod_id, pp.name period_name,    \n" +
                "                       pp.startdate::date period_start_date,    \n" +
                "                       z.id geographiczoneid,    \n" +
                "                       z.name district,    \n" +
                "                       f.name facility_name,    \n" +
                "                       f.code facility_code,     \n" +
                "                       to_char(vr.createdDate, 'DD Mon YYYY') reported_date,\n" +
                "                     CASE    \n" +
                "                        WHEN date_part('day'::text,  COALESCE(vr.statussubmissioneddate::date,vr.createddate::date) - pp.enddate::date::timestamp without time zone) <=   \n" +
                "                        COALESCE((( SELECT configuration_settings.value FROM configuration_settings     \n" +
                "                        wHERE  configuration_settings.key::text = 'LATE_REPORTING_DAYS'::text))::integer, 0)::double precision   \n" +
                "                        THEN 'T'::text     \n" +
                "                        WHEN COALESCE(date_part('day'::text,  COALESCE(vr.statussubmissioneddate::date,vr.createddate::date) - pp.enddate::date::timestamp without time zone), 0::double precision) >   \n" +
                "                        COALESCE((( SELECT configuration_settings.value FROM configuration_settings     \n" +
                "                        wHERE  configuration_settings.key::text = 'LATE_REPORTING_DAYS'::text))::integer, 0)::double precision   \n" +
                "                        THEN 'L'::text     \n" +
                "                        ELSE 'N'::text     \n" +
                "                     END AS reporting_status    \n" +
                "                      from programs_supported ps     \n" +
                "                      join vw_requisitions_submitted_status vr on vr.programid = ps.programid and vr.facilityid = ps.facilityid and status not in ('INITIATED', 'SUBMITTED', 'SKIPPED')    \n" +
                "                      join processing_periods pp on pp.id = vr.periodid    \n" +
                "                      right join facilities f on f.id = ps.facilityId      \n" +
                "                      and pp.startdate::date >= '"+params.getPeriodStart()+"'::date and pp.enddate::date <= '"+params.getPeriodEnd()+"'::date   \n" +
                "                      join geographic_zones z on z.id = f.geographicZoneId     \n" +
                "                      )      \n" +
                "\n" +
                "                      select     \n" +
                "                      vd.region_name,    \n" +
                "                      vd.district_name,  \n" +
                "                      priod_id,        \n" +
                "                      t.period_name,    \n" +
                "                      t.period_start_date,   \n" +
                "                      t.geographiczoneid,\n" +
                "                      sum(case when reporting_status IN ('T','L') then 1 else 0 end) reported,    \n" +
                "                      sum(case when reporting_status = 'T' then 1 else 0 end) ontime,     \n" +
                "                      sum(case when reporting_status = 'L' then 1 else 0 end) late   \n" +
                "                    from temp t    \n" +
                "                        join vw_districts vd on vd.district_id = t.geographiczoneid   \n" +
                "                       where vd.district_id in (select district_id from vw_user_facilities where user_id =  " + params.getUserId() + "::int   and program_id =  " + params.getProgram() + "::int)  \n" +
               writeDistrictPredicate(params.getDistrict())+
                "                     group by 1, 2, 3, 4,5 ,6   \n" +
                "                  ) a    \n" +
                "                     ),   \n" +
                "\n" +
                "                                completness_with_nonreporting_periods as (  \n" +
                "                                      select c.geographiczoneid, periods.*,  \n" +
                "                                            (select count(*) from vw_facility_start_periods fsp  \n" +
                "                        join vw_user_facilities uf on uf.facility_id = fsp.facilityid and uf.program_id =  " + params.getProgram() + "::int and uf.user_id =  " + params.getUserId() + "::int   \n" +
                "                                       where fsp.startdate <= periods.period_start_date  \n" +
                "                                         and fsp.geographiczoneid = c.geographiczoneid) expected  \n" +
                "                                        from   \n" +
                "                                         (  \n" +
                "                                              select id, name period_name, startdate period_start_date from processing_periods pp   \n" +
                "                                                where pp.startdate::date >= '"+params.getPeriodStart()+"' and pp.enddate::date <= '"+params.getPeriodEnd()+"'  \n" +
                "                                              AND pp.numberofmonths = 1  \n" +
                "                                          ) periods ,   \n" +
                "                                          (  \n" +
                "                                              select distinct geographiczoneid from   \n" +
                "                                              completeness_with_reporting_periods c  \n" +
                "                                          ) c  \n" +
                "                                    )  \n" +
                "\n" +
                "                      SELECT          \n" +
                "                      nonreporting.period_name,   \n" +
                "                      nonreporting.period_start_date, \n" +
                "                      Extract(month FROM nonreporting.period_start_date) as month, \n" +
                "                      Extract(year FROM nonreporting.period_start_date) as year, \n" +
                "                      SUM(nonreporting.expected)  expected,   \n" +
                "                      SUM(COALESCE(c.reported,0)) reported,    \n" +
                "                      SUM(COALESCE(c.ontime,0))   ontime,     \n" +
                "                      SUM(COALESCE(c.late,0))     late     \n" +
                "                      FROM completness_with_nonreporting_periods nonreporting   \n" +
                "                      join geographic_zones z on z.id = nonreporting.geographiczoneid    \n" +
                "                      join vw_districts vd on vd.district_id = nonreporting.geographiczoneid  \n" +
                "                      left outer join completeness_with_reporting_periods c  On c.geographiczoneid = nonreporting.geographiczoneid AND nonreporting.id = c.priod_id  \n" +
                "                      group by 1,2 order by 2; ";
        return sql;
    }


    private static String writeDistrictPredicate(Long zone) {

        String predicate = " ";
        if (zone != 0 && zone != null) {
            predicate = " AND (district_id = " + zone + " or zone_id = " + zone + " or region_id = " + zone + " or parent = " + zone + ")";
        }
        return predicate;
    }
}
