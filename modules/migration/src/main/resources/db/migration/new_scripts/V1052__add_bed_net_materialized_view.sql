DROP MATERIALIZED VIEW IF EXISTS public.vw_bed_nets_data;

CREATE MATERIALIZED VIEW public.vw_bed_nets_data AS
 WITH q AS (
         SELECT facilities.code AS orgunit,
            li.beginningbalance AS openingbalance,
            li.quantityreceived AS receipts,
            li.quantitydispensed AS issues,
            li.stockinhand AS closingbalance,
            (date_part('YEAR'::text, processing_periods.startdate) || ''::text) || to_char(processing_periods.enddate, 'MM'::text) AS period,
            r.periodid,
            processing_schedules.name AS schedulename,
            date_part('YEAR'::text, processing_periods.startdate) AS reporting_year,
            products.code AS productcode
           FROM facilities
             JOIN facility_types ON facility_types.id = facilities.typeid
             JOIN requisitions r ON r.facilityid = facilities.id
             JOIN requisition_line_items li ON li.rnrid = r.id
             JOIN products ON products.code::text = li.productcode::text
             JOIN vw_districts gz ON gz.district_id = facilities.geographiczoneid
             JOIN programs ON r.programid = programs.id
             JOIN program_products pps ON r.programid = pps.programid AND products.id = pps.productid
             JOIN requisition_group_members ON facilities.id = requisition_group_members.facilityid
             JOIN requisition_groups ON requisition_groups.id = requisition_group_members.requisitiongroupid
             JOIN requisition_group_program_schedules ON requisition_group_program_schedules.programid = programs.id AND requisition_group_program_schedules.requisitiongroupid = requisition_groups.id
             JOIN processing_periods ON processing_periods.id = r.periodid
             JOIN processing_schedules ON processing_schedules.id = requisition_group_program_schedules.scheduleid AND processing_schedules.id = processing_periods.scheduleid
          WHERE (r.status::text = ANY (ARRAY['APPROVED'::character varying, 'RELEASED'::character varying]::text[])) AND r.emergency = false AND (li.productcode::text IN ( SELECT products_1.code
                   FROM products products_1
                  WHERE products_1.tracknet = true AND products_1.active = true))
          ORDER BY ((date_part('YEAR'::text, processing_periods.startdate) || ''::text) || to_char(processing_periods.enddate, 'MM'::text)), processing_schedules.name
        )
 SELECT q.period::integer AS period,
    q.orgunit,
    COALESCE(q.receipts, 0) AS value,
    'LLINRECVD'::text AS dataelement,
    q.schedulename,
    q.reporting_year
   FROM q
UNION ALL
 SELECT q.period::integer AS period,
    q.orgunit,
    COALESCE(q.issues, 0) AS value,
    'LLINISSUED'::text AS dataelement,
    q.schedulename,
    q.reporting_year
   FROM q
UNION ALL
 SELECT q.period::integer AS period,
    q.orgunit,
    COALESCE(q.closingbalance, 0) AS value,
    'LLINSOH'::text AS dataelement,
    q.schedulename,
    q.reporting_year
   FROM q
WITH DATA;

ALTER TABLE public.vw_bed_nets_data
  OWNER TO postgres;