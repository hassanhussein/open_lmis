DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_reporting_rate;
 DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_stock_availablity;

 CREATE MATERIALIZED VIEW  public.mv_dashboard_reporting_rate
TABLESPACE pg_default
AS
 WITH reportingrate AS (
         SELECT gzz.id,
            gzz.name,
            COALESCE(prevexpected.count, 0::bigint) AS prevexpected,
            COALESCE(expected.count, 0::bigint) AS expected,
            COALESCE(total.count, 0::bigint) AS total,
            COALESCE(ever.count, 0::bigint) AS ever,
            COALESCE(prevperiod.count, 0::bigint) AS prevperiod,
            COALESCE(period.count, 0::bigint) AS period
           FROM geographic_zones gzz
             LEFT JOIN ( SELECT facilities.geographiczoneid,
                    count(*) AS count
                   FROM facilities
                     JOIN programs_supported ps ON ps.facilityid = facilities.id
                     JOIN geographic_zones gz ON gz.id = facilities.geographiczoneid
                     JOIN requisition_group_members rgm ON rgm.facilityid = facilities.id
                     JOIN requisition_group_program_schedules rgps ON rgps.requisitiongroupid = rgm.requisitiongroupid AND rgps.programid = ps.programid
                     JOIN processing_periods pp ON pp.scheduleid = rgps.scheduleid AND pp.id = 155
                  WHERE gz.levelid = (( SELECT max(geographic_levels.id) AS max
                           FROM geographic_levels)) AND ps.programid = 3
                  GROUP BY facilities.geographiczoneid) prevexpected ON gzz.id = prevexpected.geographiczoneid
             LEFT JOIN ( SELECT facilities.geographiczoneid,
                    count(*) AS count
                   FROM facilities
                     JOIN programs_supported ps ON ps.facilityid = facilities.id
                     JOIN geographic_zones gz ON gz.id = facilities.geographiczoneid
                     JOIN requisition_group_members rgm ON rgm.facilityid = facilities.id
                     JOIN requisition_group_program_schedules rgps ON rgps.requisitiongroupid = rgm.requisitiongroupid AND rgps.programid = ps.programid
                     JOIN processing_periods pp ON pp.scheduleid = rgps.scheduleid AND pp.id = 156
                  WHERE gz.levelid = (( SELECT max(geographic_levels.id) AS max
                           FROM geographic_levels)) AND ps.programid = 3
                  GROUP BY facilities.geographiczoneid) expected ON gzz.id = expected.geographiczoneid
             LEFT JOIN ( SELECT facilities.geographiczoneid,
                    count(*) AS count
                   FROM facilities
                     JOIN geographic_zones gz ON gz.id = facilities.geographiczoneid
                  WHERE gz.levelid = (( SELECT max(geographic_levels.id) AS max
                           FROM geographic_levels))
                  GROUP BY facilities.geographiczoneid) total ON gzz.id = total.geographiczoneid
             LEFT JOIN ( SELECT facilities.geographiczoneid,
                    count(*) AS count
                   FROM facilities
                     JOIN programs_supported ps ON ps.facilityid = facilities.id
                     JOIN geographic_zones gz ON gz.id = facilities.geographiczoneid
                  WHERE ps.programid = 3 AND (facilities.id IN ( SELECT requisitions.facilityid
                           FROM requisitions
                          WHERE requisitions.programid = 3))
                  GROUP BY facilities.geographiczoneid) ever ON gzz.id = ever.geographiczoneid
             LEFT JOIN ( SELECT facilities.geographiczoneid,
                    count(*) AS count
                   FROM facilities
                     JOIN programs_supported ps ON ps.facilityid = facilities.id
                     JOIN geographic_zones gz ON gz.id = facilities.geographiczoneid
                  WHERE ps.programid = 3 AND (facilities.id IN ( SELECT requisitions.facilityid
                           FROM requisitions
                          WHERE requisitions.periodid = 155 AND requisitions.programid = 3 AND (requisitions.status::text <> ALL (ARRAY['INITIATED'::character varying::text, 'SUBMITTED'::character varying::text, 'SKIPPED'::character varying::text])) AND requisitions.emergency = false))
                  GROUP BY facilities.geographiczoneid) prevperiod ON gzz.id = prevperiod.geographiczoneid
             LEFT JOIN ( SELECT facilities.geographiczoneid,
                    count(*) AS count
                   FROM facilities
                     JOIN programs_supported ps ON ps.facilityid = facilities.id
                     JOIN geographic_zones gz ON gz.id = facilities.geographiczoneid
                  WHERE ps.programid = 3 AND (facilities.id IN ( SELECT requisitions.facilityid
                           FROM requisitions
                          WHERE requisitions.periodid = 156 AND requisitions.programid = 3 AND (requisitions.status::text <> ALL (ARRAY['INITIATED'::character varying::text, 'SUBMITTED'::character varying::text, 'SKIPPED'::character varying::text])) AND requisitions.emergency = false))
                  GROUP BY facilities.geographiczoneid) period ON gzz.id = period.geographiczoneid
          ORDER BY gzz.name
        ), aggeregate AS (
         SELECT d.region_name AS name,
            sum(r.prevexpected) AS prevexpected,
            sum(r.period) AS period,
            sum(r.expected) AS expected
           FROM reportingrate r
             JOIN vw_districts d ON d.district_id = r.id
          GROUP BY d.region_name
        )
 SELECT a.name,
    a.prevexpected AS prev,
    a.period AS current,
        CASE
            WHEN
            CASE
                WHEN COALESCE(a.expected, 0::numeric) = 0::numeric THEN 0::numeric
                ELSE round(COALESCE(a.period, 0::numeric) / COALESCE(a.expected, 0::numeric) * 100::numeric, 2)
            END >= 80::numeric THEN 'good'::text
            WHEN
            CASE
                WHEN COALESCE(a.expected, 0::numeric) = 0::numeric THEN 0::numeric
                ELSE round(COALESCE(a.period, 0::numeric) / COALESCE(a.expected, 0::numeric) * 100::numeric, 2)
            END >= 60::numeric THEN 'normal'::text
            WHEN
            CASE
                WHEN COALESCE(a.expected, 0::numeric) = 0::numeric THEN 0::numeric
                ELSE round(COALESCE(a.period, 0::numeric) / COALESCE(a.expected, 0::numeric) * 100::numeric, 2)
            END < 60::numeric THEN 'bad'::text
            ELSE NULL::text
        END AS status
   FROM aggeregate a
  ORDER BY a.name
  WITH  DATA;



 CREATE MATERIALIZED VIEW public.mv_dashboard_stock_availablity
    TABLESPACE pg_default
    AS
 WITH stockout AS (
             SELECT vw_stock_status_2.facility_id AS facilityid,
                count(*) AS count
               FROM vw_stock_status_2
              WHERE vw_stock_status_2.periodid = 155 AND vw_stock_status_2.programid = 1 AND (vw_stock_status_2.gz_id = 0 OR 0 = 0) AND vw_stock_status_2.req_status::text <> 'INITIATED'::text AND vw_stock_status_2.reported_figures > 0 AND vw_stock_status_2.status = 'SO'::text
              GROUP BY vw_stock_status_2.facility_id
            ), prevstockout AS (
             SELECT vw_stock_status_2.facility_id AS facilityid,
                count(*) AS count
               FROM vw_stock_status_2
              WHERE vw_stock_status_2.periodid = 156 AND vw_stock_status_2.programid = 1 AND (vw_stock_status_2.gz_id = 0 OR 0 = 0) AND vw_stock_status_2.req_status::text <> 'INITIATED'::text AND vw_stock_status_2.reported_figures > 0 AND vw_stock_status_2.status = 'SO'::text
              GROUP BY vw_stock_status_2.facility_id
            ), compiled AS (
             SELECT d.region_id AS region,
                d.region_name AS name,
                sum(ps.count) AS prev,
                sum(s.count) AS current,
                count(d.region_id) AS total
               FROM facilities f
                 JOIN vw_districts d ON f.geographiczoneid = d.district_id
                 LEFT JOIN stockout s ON f.id = s.facilityid
                 LEFT JOIN prevstockout ps ON f.id = ps.facilityid
              GROUP BY d.region_id, d.region_name
            )
     SELECT c.name,
        round(100::numeric - c.prev / c.total::numeric, 2) AS prev,
        round(100::numeric - c.current / c.total::numeric, 2) AS current,
            CASE
                WHEN sum(100::numeric - c.current / c.total::numeric) >= 80::numeric THEN 'good'::text
                WHEN sum(100::numeric - c.current / c.total::numeric) >= 60::numeric THEN 'normal'::text
                WHEN sum(100::numeric - c.current / c.total::numeric) < 60::numeric THEN 'bad'::text
                ELSE NULL::text
            END AS status
       FROM compiled c
      GROUP BY c.name, c.prev, c.current, c.total
      ORDER BY c.name
WITH DATA;