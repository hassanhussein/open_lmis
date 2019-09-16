DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_timeliness_report;

CREATE MATERIALIZED VIEW public.mv_dashboard_timeliness_report AS
 SELECT requisitions.programid,
    requisitions.periodid,
    rgps.scheduleid,
    facilities.geographiczoneid,
    facilities.name AS facilityname,
    facilities.code AS facilitycode,
    requisitions.createddate,
    pp.enddate,
    requisitions.status,
    requisitions.id AS rnrid,
    facilities.id AS facilityid,
    facility_types.id AS typeid,
    facility_types.name AS facilitytypename,
        CASE
            WHEN COALESCE(date_part('day'::text, requisitions.createddate - pp.enddate::date::timestamp without time zone), 0::double precision) <= COALESCE((( SELECT configuration_settings.value
               FROM configuration_settings
              WHERE configuration_settings.key::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'R'::text
            WHEN COALESCE(date_part('day'::text, requisitions.createddate - pp.enddate::date::timestamp without time zone), 0::double precision) > COALESCE((( SELECT configuration_settings.value
               FROM configuration_settings
              WHERE configuration_settings.key::text = 'UNSCHEDULED_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'U'::text
            WHEN COALESCE(date_part('day'::text, requisitions.createddate - pp.enddate::date::timestamp without time zone), 0::double precision) > COALESCE((( SELECT configuration_settings.value
               FROM configuration_settings
              WHERE configuration_settings.key::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'L'::text
            ELSE 'N'::text
        END AS reportingstatus,
        CASE
            WHEN COALESCE(date_part('day'::text, requisitions.createddate - pp.enddate::date::timestamp without time zone), 0::double precision) <= COALESCE((( SELECT configuration_settings.value
               FROM configuration_settings
              WHERE configuration_settings.key::text = 'REDESIGNED_DISTRICT_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'R'::text
            WHEN COALESCE(date_part('day'::text, requisitions.createddate - pp.enddate::date::timestamp without time zone), 0::double precision) > COALESCE((( SELECT configuration_settings.value
               FROM configuration_settings
              WHERE configuration_settings.key::text = 'UNSCHEDULED_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'U'::text
            WHEN COALESCE(date_part('day'::text, requisitions.createddate - pp.enddate::date::timestamp without time zone), 0::double precision) > COALESCE((( SELECT configuration_settings.value
               FROM configuration_settings
              WHERE configuration_settings.key::text = 'REDESIGNED_DISTRICT_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'L'::text
            ELSE 'N'::text
        END AS redesignreportingstatus
   FROM requisitions
     JOIN facilities ON requisitions.facilityid = facilities.id
     JOIN requisition_group_members rgm ON rgm.facilityid = requisitions.facilityid
     JOIN facility_types ON facilities.typeid = facility_types.id
     JOIN programs_supported ps ON ps.programid = requisitions.programid AND requisitions.facilityid = ps.facilityid
     JOIN processing_periods pp ON pp.id = requisitions.periodid
     JOIN requisition_group_program_schedules rgps ON rgps.requisitiongroupid = rgm.requisitiongroupid AND rgps.programid = requisitions.programid AND pp.scheduleid = rgps.scheduleid
     JOIN geographic_zones ON facilities.geographiczoneid = geographic_zones.id
  WHERE (requisitions.status::text = ANY (ARRAY['AUTHORIZED'::text, 'IN_APPROVAL'::character varying::text, 'APPROVED'::character varying::text, 'RELEASED'::character varying::text])) AND facilities.active = true AND requisitions.emergency = false
  GROUP BY requisitions.status, requisitions.createddate, pp.enddate, requisitions.id, requisitions.programid, requisitions.periodid, rgps.scheduleid, facilities.geographiczoneid, facilities.name, facilities.code, facilities.id, facility_types.id, facility_types.name
WITH NO DATA;



DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_wastage_line_items;

CREATE MATERIALIZED VIEW public.mv_dashboard_wastage_line_items AS
 SELECT requisitions.programid,
    requisitions.periodid,
    requisition_line_items.beginningbalance + requisition_line_items.quantityreceived AS total,
    requisition_line_item_losses_adjustments.quantity AS adjustment_qty,
    losses_adjustments_types.name AS adjustmentname
   FROM requisition_line_items
     JOIN requisitions ON requisition_line_items.rnrid = requisitions.id
     JOIN requisition_line_item_losses_adjustments ON requisition_line_items.id = requisition_line_item_losses_adjustments.requisitionlineitemid
     JOIN losses_adjustments_types ON requisition_line_item_losses_adjustments.type::text = losses_adjustments_types.name::text
  WHERE losses_adjustments_types.wastage = true AND requisition_line_items.skipped = false AND requisitions.emergency = false AND (requisitions.status::text = ANY (ARRAY['AUTHORIZED'::character varying::text, 'APPROVED'::character varying::text, 'RELEASED'::character varying::text]))
WITH NO DATA;

ALTER TABLE public.mv_dashboard_wastage_line_items
  OWNER TO postgres;

-- Index: public.i_mv_dashboard_wastage_line_items_adjustmentname

-- DROP INDEX public.i_mv_dashboard_wastage_line_items_adjustmentname;

CREATE INDEX i_mv_dashboard_wastage_line_items_adjustmentname
  ON public.mv_dashboard_wastage_line_items
  USING btree
  (adjustmentname COLLATE pg_catalog."default");


DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_consumption_summary;

CREATE MATERIALIZED VIEW public.mv_dashboard_consumption_summary AS
 SELECT DISTINCT p.code AS productcode,
    p.id AS productid,
    r.programid,
    r.periodid,
    p.primaryname AS productname,
    pr.name AS periodname,
        CASE
            WHEN i.stockinhand = 0 THEN 'SO'::text
            ELSE
            CASE
                WHEN i.amc > 0 AND i.stockinhand > 0 THEN
                CASE
                    WHEN round(i.stockinhand::numeric / i.amc::numeric, 2) < fap.minmonthsofstock THEN 'US'::text
                    WHEN round(i.stockinhand::numeric / i.amc::numeric, 2) >= fap.minmonthsofstock AND round(i.stockinhand::numeric / i.amc::numeric, 2) <= fap.maxmonthsofstock::numeric THEN 'SP'::text
                    WHEN round(i.stockinhand::numeric / i.amc::numeric, 2) > fap.maxmonthsofstock::numeric THEN 'OS'::text
                    ELSE NULL::text
                END
                ELSE 'UK'::text
            END
        END AS status,
        CASE
            WHEN COALESCE(i.amc, 0) = 0 THEN 0::numeric
            ELSE trunc(round(i.stockinhand::numeric / i.amc::numeric, 2), 1)
        END AS mos,
    i.amc,
    i.stockinhand,
    date_part('year'::text, pr.startdate) AS year,
    pr.scheduleid
   FROM products p
     JOIN program_products pp ON p.id = pp.productid
     JOIN programs ON pp.programid = programs.id
     JOIN requisitions r ON r.programid = pp.programid
     JOIN facilities f ON f.id = r.facilityid
     JOIN facility_types ft ON ft.id = f.typeid
     JOIN requisition_line_items i ON i.rnrid = r.id AND i.productcode::text = p.code::text
     JOIN processing_periods pr ON r.periodid = pr.id
     JOIN facility_approved_products fap ON ft.id = fap.facilitytypeid AND fap.programproductid = pp.id
  WHERE COALESCE(i.stockinhand, 0) > 0 AND r.emergency = false AND i.skipped = false
WITH NO DATA;

ALTER TABLE public.mv_dashboard_consumption_summary
  OWNER TO postgres;

-- Index: public.i_mv_dashboard_consumption_summary_productcode

-- DROP INDEX public.i_mv_dashboard_consumption_summary_productcode;

CREATE INDEX i_mv_dashboard_consumption_summary_productcode
  ON public.mv_dashboard_consumption_summary
  USING btree
  (productcode COLLATE pg_catalog."default");

-- Index: public.i_mv_dashboard_consumption_summary_programs_periodname

-- DROP INDEX public.i_mv_dashboard_consumption_summary_programs_periodname;

CREATE INDEX i_mv_dashboard_consumption_summary_programs_periodname
  ON public.mv_dashboard_consumption_summary
  USING btree
  (periodname COLLATE pg_catalog."default");

-- Index: public.i_mv_dashboard_consumption_summary_programs_productname

-- DROP INDEX public.i_mv_dashboard_consumption_summary_programs_productname;

CREATE INDEX i_mv_dashboard_consumption_summary_programs_productname
  ON public.mv_dashboard_consumption_summary
  USING btree
  (productname COLLATE pg_catalog."default");











