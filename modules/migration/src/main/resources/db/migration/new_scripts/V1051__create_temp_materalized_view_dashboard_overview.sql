-- View: public.mv_dashboard_reporting_rate

DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_reporting_rate;
DROP MATERIALIZED VIEW IF EXISTS public.mv_order_fill_rate;
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
                     JOIN processing_periods pp ON pp.scheduleid = rgps.scheduleid AND pp.id = 112
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
                     JOIN processing_periods pp ON pp.scheduleid = rgps.scheduleid AND pp.id = 114
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
                          WHERE requisitions.periodid = 112 AND requisitions.programid = 3 AND (requisitions.status::text <> ALL (ARRAY['INITIATED'::character varying, 'SUBMITTED'::character varying, 'SKIPPED'::character varying]::text[])) AND requisitions.emergency = false))
                  GROUP BY facilities.geographiczoneid) prevperiod ON gzz.id = prevperiod.geographiczoneid
             LEFT JOIN ( SELECT facilities.geographiczoneid,
                    count(*) AS count
                   FROM facilities
                     JOIN programs_supported ps ON ps.facilityid = facilities.id
                     JOIN geographic_zones gz ON gz.id = facilities.geographiczoneid
                  WHERE ps.programid = 3 AND (facilities.id IN ( SELECT requisitions.facilityid
                           FROM requisitions
                          WHERE requisitions.periodid = 113 AND requisitions.programid = 3 AND (requisitions.status::text <> ALL (ARRAY['INITIATED'::character varying, 'SUBMITTED'::character varying, 'SKIPPED'::character varying]::text[])) AND requisitions.emergency = false))
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
WITH DATA;

ALTER TABLE public.mv_dashboard_reporting_rate
    OWNER TO postgres;

    -- View: public.mv_dashboard_stock_availablity


    CREATE MATERIALIZED VIEW public.mv_dashboard_stock_availablity
    TABLESPACE pg_default
    AS
     WITH stockout AS (
             SELECT vw_stock_status_2.facility_id AS facilityid,
                count(*) AS count
               FROM vw_stock_status_2
              WHERE vw_stock_status_2.periodid = 113 AND vw_stock_status_2.programid = 1 AND (vw_stock_status_2.gz_id = 0 OR 0 = 0) AND vw_stock_status_2.req_status::text <> 'INITIATED'::text AND vw_stock_status_2.reported_figures > 0 AND vw_stock_status_2.status = 'SO'::text
              GROUP BY vw_stock_status_2.facility_id
            ), prevstockout AS (
             SELECT vw_stock_status_2.facility_id AS facilityid,
                count(*) AS count
               FROM vw_stock_status_2
              WHERE vw_stock_status_2.periodid = 114 AND vw_stock_status_2.programid = 1 AND (vw_stock_status_2.gz_id = 0 OR 0 = 0) AND vw_stock_status_2.req_status::text <> 'INITIATED'::text AND vw_stock_status_2.reported_figures > 0 AND vw_stock_status_2.status = 'SO'::text
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

    ALTER TABLE public.mv_dashboard_stock_availablity
        OWNER TO postgres;



        -- View: public.mv_order_fill_rate

        -- DROP MATERIALIZED VIEW public.mv_order_fill_rate;

        CREATE MATERIALIZED VIEW public.mv_order_fill_rate
        TABLESPACE pg_default
        AS
         WITH fullfilment AS (
                 SELECT DISTINCT substitutes.orderid,
                    substitutes.productcode,
                    substitutes.quantityshipped,
                    substitutes.substitutedproductcode,
                    substitutes.substitutedproductname,
                    substitutes.substitutedproductquantityshipped
                   FROM ( SELECT NULL::integer AS orderid,
                            NULL::bigint AS quantityshipped,
                            li.productcode,
                            li.substitutedproductcode,
                            li.substitutedproductname,
                            li.substitutedproductquantityshipped
                           FROM shipment_line_items li
                          WHERE li.substitutedproductcode IS NOT NULL
                        UNION
                         SELECT shipment_line_items.orderid,
                            sum(shipment_line_items.quantityshipped) AS quantityshipped,
                            shipment_line_items.productcode,
                            NULL::character varying AS substitutedproductcode,
                            NULL::character varying AS substitutedproductname,
                            NULL::integer AS substitutedproductquantityshipped
                           FROM shipment_line_items
                          GROUP BY shipment_line_items.orderid, shipment_line_items.productcode) substitutes
                  ORDER BY substitutes.productcode, substitutes.substitutedproductcode DESC
                ), request AS (
                 SELECT r.id AS rnrid,
                    li.productcode,
                    fa.id AS facilityid,
                    fa.geographiczoneid AS location,
                    li.quantityrequested AS "order",
                    li.quantityapproved AS approved,
                        CASE
                            WHEN COALESCE(li.quantityapproved::numeric, 0::numeric) = 0::numeric THEN 0::numeric
                            ELSE round(COALESCE(f.quantityshipped, 0::bigint)::numeric / COALESCE(li.quantityapproved, 0)::numeric * 100::numeric, 2)
                        END AS item_fill_rate
                   FROM requisitions r
                     JOIN requisition_line_items li ON r.id = li.rnrid
                     JOIN facilities fa ON fa.id = r.facilityid
                     LEFT JOIN fullfilment f ON f.productcode::text = li.productcode::text AND f.orderid = li.rnrid
                  WHERE r.periodid = 110 AND r.programid = 3
                ), prev AS (
                 SELECT r.id AS rnrid,
                    r.periodid,
                    li.productcode,
                    fa.id AS facilityid,
                    fa.geographiczoneid AS location,
                    li.quantityrequested AS "order",
                    li.quantityapproved AS approved,
                        CASE
                            WHEN COALESCE(li.quantityapproved::numeric, 0::numeric) = 0::numeric THEN 0::numeric
                            ELSE round(COALESCE(f.quantityshipped, 0::bigint)::numeric / COALESCE(li.quantityapproved, 0)::numeric * 100::numeric, 2)
                        END AS item_fill_rate
                   FROM requisitions r
                     JOIN requisition_line_items li ON r.id = li.rnrid
                     JOIN facilities fa ON fa.id = r.facilityid
                     LEFT JOIN fullfilment f ON f.productcode::text = li.productcode::text AND f.orderid = li.rnrid
                  WHERE r.periodid = 113 AND r.programid = 3
                ), aggregate AS (
                 SELECT d.region_name AS name,
                    sum(p.item_fill_rate) AS prev,
                    sum(c.item_fill_rate) AS current,
                    count(*) AS count
                   FROM request c
                     JOIN prev p ON c.facilityid = p.facilityid AND p.productcode::text = c.productcode::text
                     JOIN vw_districts d ON d.district_id = p.location
                  GROUP BY d.region_name
                )
         SELECT a.name,
            round(a.prev / a.count::numeric, 2) AS prev,
            round(a.current / a.count::numeric, 2) AS current,
                CASE
                    WHEN round(a.current / a.count::numeric, 2) >= 80::numeric THEN 'good'::text
                    WHEN round(a.current / a.count::numeric, 2) >= 60::numeric THEN 'normal'::text
                    WHEN round(a.current / a.count::numeric, 2) < 60::numeric THEN 'bad'::text
                    ELSE NULL::text
                END AS status
           FROM aggregate a
          ORDER BY a.name
        WITH DATA;

        ALTER TABLE public.mv_order_fill_rate
            OWNER TO postgres;