-- Start adding Dashboard views

-- View: public.mv_dashboard_tracer_available_by_programs

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_tracer_available_by_programs;

CREATE MATERIALIZED VIEW public.mv_dashboard_tracer_available_by_programs
    TABLESPACE pg_default
AS
SELECT DISTINCT i.productcode,
                programs.name AS program_name,
                r.programid,
                r.periodid,
                pr.name AS periodname
FROM (((((products p
    JOIN program_products pp ON ((p.id = pp.productid)))
    JOIN programs ON ((pp.programid = programs.id)))
    JOIN requisitions r ON ((r.programid = pp.programid)))
    JOIN requisition_line_items i ON (((i.rnrid = r.id) AND ((i.productcode)::text = (p.code)::text))))
         JOIN processing_periods pr ON ((r.periodid = pr.id)))
WHERE ((p.tracer = true) AND (i.stockinhand > 0))
WITH DATA;

ALTER TABLE public.mv_dashboard_tracer_available_by_programs
    OWNER TO postgres;


CREATE INDEX i_mv_dashboard_tracer_available_by_programs_periodid
    ON public.mv_dashboard_tracer_available_by_programs USING btree
        (periodid)
    TABLESPACE pg_default;
CREATE INDEX i_mv_dashboard_tracer_available_by_programs_productcode
    ON public.mv_dashboard_tracer_available_by_programs USING btree
        (productcode COLLATE pg_catalog."default")
    TABLESPACE pg_default;


-- View: public.mv_dashboard_tracer_product_by_program_counts

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_tracer_product_by_program_counts;

CREATE MATERIALIZED VIEW public.mv_dashboard_tracer_product_by_program_counts
    TABLESPACE pg_default
AS
SELECT pp.programid,
       programs.name,
       count(*) AS total
FROM ((products p
    JOIN program_products pp ON ((p.id = pp.productid)))
         JOIN programs ON ((pp.programid = programs.id)))
WHERE (p.tracer = true)
GROUP BY pp.programid, programs.name
WITH DATA;

ALTER TABLE public.mv_dashboard_tracer_product_by_program_counts
    OWNER TO postgres;

-- View: public.mv_dashboard_tracer_reported_products_by_programs

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_tracer_reported_products_by_programs;

CREATE MATERIALIZED VIEW public.mv_dashboard_tracer_reported_products_by_programs
    TABLESPACE pg_default
AS
SELECT DISTINCT p.code AS productcode,
                r.programid,
                r.periodid,
                p.primaryname AS productname,
                CASE
                    WHEN (i.stockinhand = 0) THEN 'SO'::text
                    ELSE
                        CASE
                            WHEN ((i.amc > 0) AND (i.stockinhand > 0)) THEN
                                CASE
                                    WHEN (round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) < fap.minmonthsofstock) THEN 'US'::text
                                    WHEN ((round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) >= fap.minmonthsofstock) AND (round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) <= (fap.maxmonthsofstock)::numeric)) THEN 'SP'::text
                                    WHEN (round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) > (fap.maxmonthsofstock)::numeric) THEN 'OS'::text
                                    ELSE NULL::text
                                    END
                            ELSE 'UK'::text
                            END
                    END AS status,
                CASE
                    WHEN (COALESCE(i.amc, 0) = 0) THEN (0)::numeric
                    ELSE trunc(round(((i.stockinhand)::numeric / (i.amc)::numeric), 2), 1)
                    END AS mos,
                i.amc,
                i.stockinhand
FROM ((((((((products p
    JOIN program_products pp ON ((p.id = pp.productid)))
    JOIN programs ON ((pp.programid = programs.id)))
    JOIN requisitions r ON ((r.programid = pp.programid)))
    JOIN facilities f ON ((f.id = r.facilityid)))
    JOIN facility_types ft ON ((ft.id = f.typeid)))
    JOIN requisition_line_items i ON (((i.rnrid = r.id) AND ((i.productcode)::text = (p.code)::text))))
    JOIN processing_periods pr ON ((r.periodid = pr.id)))
         JOIN facility_approved_products fap ON (((ft.id = fap.facilitytypeid) AND (fap.programproductid = pp.id))))
WHERE ((p.tracer = true) AND (COALESCE(i.stockinhand, 0) > 0) AND (r.emergency = false) AND (i.skipped = false))
WITH DATA;

ALTER TABLE public.mv_dashboard_tracer_reported_products_by_programs
    OWNER TO postgres;


CREATE INDEX i_mv_dashboard_tracer_reported_products_by_programs_productcode
    ON public.mv_dashboard_tracer_reported_products_by_programs USING btree
        (productcode COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX i_mv_dashboard_tracer_reported_products_by_programs_productname
    ON public.mv_dashboard_tracer_reported_products_by_programs USING btree
        (productname COLLATE pg_catalog."default")
    TABLESPACE pg_default;


-- View: public.mv_dashboard_wastage_line_items

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_wastage_line_items;

CREATE MATERIALIZED VIEW public.mv_dashboard_wastage_line_items
    TABLESPACE pg_default
AS
SELECT requisitions.programid,
       requisitions.periodid,
       (requisition_line_items.beginningbalance + requisition_line_items.quantityreceived) AS total,
       requisition_line_item_losses_adjustments.quantity AS adjustment_qty,
       losses_adjustments_types.name AS adjustmentname
FROM (((requisition_line_items
    JOIN requisitions ON ((requisition_line_items.rnrid = requisitions.id)))
    JOIN requisition_line_item_losses_adjustments ON ((requisition_line_items.id = requisition_line_item_losses_adjustments.requisitionlineitemid)))
         JOIN losses_adjustments_types ON (((requisition_line_item_losses_adjustments.type)::text = (losses_adjustments_types.name)::text)))
WHERE ( (requisition_line_items.skipped = false) AND (requisitions.emergency = false) AND ((requisitions.status)::text = ANY (ARRAY[('AUTHORIZED'::character varying)::text, ('APPROVED'::character varying)::text, ('RELEASED'::character varying)::text])))
WITH DATA;

ALTER TABLE public.mv_dashboard_wastage_line_items
    OWNER TO postgres;


CREATE INDEX i_mv_dashboard_wastage_line_items_adjustmentname
    ON public.mv_dashboard_wastage_line_items USING btree
        (adjustmentname COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- View: public.mv_latest_reported_stock_status

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_latest_reported_stock_status;

CREATE MATERIALIZED VIEW public.mv_latest_reported_stock_status
    TABLESPACE pg_default
AS
SELECT rli.productcode,
       p.id AS productid,
       p.tracer,
       p.primaryname,
       gz.district_name,
       gz.region_name,
       sum(
               CASE
                   WHEN (rli.stockinhand = 0) THEN 1
                   ELSE 0
                   END) AS stockoutincidence,
       sum(
               CASE
                   WHEN (rli.skipped = true) THEN 1
                   ELSE 0
                   END) AS skipped,
       max(pp.id) AS periodid,
       count(*) AS totalincidence
FROM (((((requisition_line_items rli
    JOIN products p ON (((p.code)::text = (rli.productcode)::text)))
    JOIN requisitions r ON ((r.id = rli.rnrid)))
    JOIN processing_periods pp ON ((pp.id = r.periodid)))
    JOIN facilities f ON ((f.id = r.facilityid)))
         JOIN vw_districts gz ON ((gz.district_id = f.geographiczoneid)))
WHERE ((r.id, r.facilityid) IN ( SELECT max(requisitions.id) AS max,
                                        requisitions.facilityid
                                 FROM requisitions
                                 WHERE (((requisitions.status)::text = 'RELEASED'::text) AND (requisitions.emergency = false))
                                 GROUP BY requisitions.facilityid))
GROUP BY rli.productcode, p.id, p.tracer, p.primaryname, gz.district_name, gz.region_name
WITH DATA;

ALTER TABLE public.mv_latest_reported_stock_status
    OWNER TO postgres;


CREATE INDEX mv_latest_reported_stock_status_district_name_index
    ON public.mv_latest_reported_stock_status USING btree
        (district_name COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX mv_latest_reported_stock_status_productcode_index
    ON public.mv_latest_reported_stock_status USING btree
        (productcode COLLATE pg_catalog."default")
    TABLESPACE pg_default;

-- View: public.mv_logistics_summary_report

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_logistics_summary_report;

CREATE MATERIALIZED VIEW public.mv_logistics_summary_report
    TABLESPACE pg_default
AS
SELECT li.productcode AS code,
       li.product,
       facilities.code AS facilitycode,
       facilities.name AS facility,
       facility_types.name AS facilitytype,
       li.productcategory AS category,
       li.dispensingunit AS unit,
       li.beginningbalance AS openingbalance,
       li.quantityreceived AS receipts,
       li.quantitydispensed AS issues,
       li.quantityapproved AS reorderamount,
       li.totallossesandadjustments AS adjustments,
       li.stockinhand AS closingbalance,
       r.periodid,
       r.programid,
       facilities.id AS facility_id,
       gz.zone_id,
       gz.parent,
       gz.region_id,
       gz.district_id,
       r.emergency,
       li.skipped,
       facilities.typeid AS facility_type_id,
       pps.productcategoryid,
       products.id AS productid
FROM ((((((((((((facilities
    JOIN facility_types ON ((facility_types.id = facilities.typeid)))
    JOIN requisitions r ON ((r.facilityid = facilities.id)))
    JOIN requisition_line_items li ON ((li.rnrid = r.id)))
    JOIN products ON (((products.code)::text = (li.productcode)::text)))
    JOIN vw_districts gz ON ((gz.district_id = facilities.geographiczoneid)))
    JOIN programs ON ((r.programid = programs.id)))
    JOIN program_products pps ON (((r.programid = pps.programid) AND (products.id = pps.productid))))
    JOIN requisition_group_members ON ((facilities.id = requisition_group_members.facilityid)))
    JOIN requisition_groups ON ((requisition_groups.id = requisition_group_members.requisitiongroupid)))
    JOIN requisition_group_program_schedules ON (((requisition_group_program_schedules.programid = programs.id) AND (requisition_group_program_schedules.requisitiongroupid = requisition_groups.id))))
    JOIN processing_schedules ON ((processing_schedules.id = requisition_group_program_schedules.scheduleid)))
         JOIN processing_periods ON ((processing_periods.id = r.periodid)))
WHERE ((r.status)::text = ANY (ARRAY[('AUTHORIZED'::character varying)::text, ('APPROVED'::character varying)::text, ('RELEASED'::character varying)::text]))
WITH DATA;

ALTER TABLE public.mv_logistics_summary_report
    OWNER TO postgres;


CREATE INDEX mv_logistics_summary_report_facility_code
    ON public.mv_logistics_summary_report USING btree
        (facilitycode COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX mv_logistics_summary_report_facility_id
    ON public.mv_logistics_summary_report USING btree
        (facility_id)
    TABLESPACE pg_default;
CREATE INDEX mv_logistics_summary_report_periodid
    ON public.mv_logistics_summary_report USING btree
        (periodid)
    TABLESPACE pg_default;
CREATE INDEX mv_logistics_summary_report_product
    ON public.mv_logistics_summary_report USING btree
        (product COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX mv_logistics_summary_report_productid
    ON public.mv_logistics_summary_report USING btree
        (productid)
    TABLESPACE pg_default;

-- View: public.mv_stock_imbalance_by_facility_report

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_stock_imbalance_by_facility_report;

CREATE MATERIALIZED VIEW public.mv_stock_imbalance_by_facility_report
    TABLESPACE pg_default
AS
SELECT gz.region_name AS supplyingfacility,
       gz.region_name,
       gz.district_name,
       gz.zone_name,
       f.code AS facilitycode,
       li.productcode,
       f.name AS facility,
       li.product,
       ft.name AS facilitytypename,
       gz.district_name AS location,
       pp.name AS processing_period_name,
       li.stockinhand,
       li.stockoutdays,
       to_char(pp.startdate, 'Mon'::text) AS asmonth,
       date_part('year'::text, pp.startdate) AS year,
       r.periodid,
       r.programid,
       f.id AS facility_id,
       gz.zone_id,
       gz.parent,
       gz.region_id,
       gz.district_id,
       r.emergency,
       li.skipped,
       pg.code AS program,
       f.typeid AS facility_type_id,
       null isprimaryhealthcare,
       pgp.productcategoryid,
       p.id AS productid,
       p.tracer,
       p.fullsupply,
       CASE
           WHEN (COALESCE(li.amc, 0) = 0) THEN 'UK'::text
           WHEN (COALESCE(li.stockinhand, 0) = 0) THEN 'SO'::text
           WHEN (round(((li.stockinhand / li.amc))::numeric, 1) < fap.minmonthsofstock) THEN 'US'::text
           WHEN (round(((li.stockinhand / li.amc))::numeric, 1) <= (fap.maxmonthsofstock)::numeric) THEN 'SP'::text
           WHEN (round(((li.stockinhand / li.amc))::numeric, 1) > fap.minmonthsofstock) THEN 'OS'::text
           ELSE 'UK'::text
           END AS status,
       CASE
           WHEN (COALESCE(li.amc, 0) = 0) THEN (0)::numeric
           ELSE trunc(round(((li.stockinhand)::numeric / (li.amc)::numeric), 2), 1)
           END AS mos,
       li.amc,
       COALESCE(
               CASE
                   WHEN (((COALESCE(li.amc, 0) * ft.nominalmaxmonth) - li.stockinhand) < 0) THEN 0
                   ELSE ((COALESCE(li.amc, 0) * ft.nominalmaxmonth) - li.stockinhand)
                   END, 0) AS required,
       li.quantityapproved AS ordered,
       ps.name AS schedule,
       ps.id AS scheduleid,
       f.longitude,
       f.latitude,
       f.mainphone,
       pgp.currentprice
FROM ((((((((((processing_periods pp
    JOIN processing_schedules ps ON ((pp.scheduleid = ps.id)))
    JOIN requisitions r ON ((pp.id = r.periodid)))
    JOIN requisition_line_items li ON ((li.rnrid = r.id)))
    JOIN facilities f ON ((f.id = r.facilityid)))
    JOIN facility_types ft ON ((ft.id = f.typeid)))
    JOIN products p ON (((p.code)::text = (li.productcode)::text)))
    JOIN vw_districts gz ON ((gz.district_id = f.geographiczoneid)))
    JOIN programs pg ON ((pg.id = r.programid)))
    JOIN program_products pgp ON (((r.programid = pgp.programid) AND (p.id = pgp.productid))))
         JOIN facility_approved_products fap ON (((ft.id = fap.facilitytypeid) AND (fap.programproductid = pgp.id))))
WHERE ((li.skipped = false) AND ((li.beginningbalance > 0) OR (li.quantityreceived > 0) OR (li.quantitydispensed > 0) OR (abs(li.totallossesandadjustments) > 0) OR (li.amc > 0)) AND ((li.beginningbalance > 0) OR (li.quantityreceived > 0) OR (li.quantitydispensed > 0) OR (abs(li.totallossesandadjustments) > 0) OR (li.amc > 0)) AND ((r.status)::text = ANY (ARRAY[('IN_APPROVAL'::character varying)::text, ('RELEASED'::character varying)::text, ('APPROVED'::character varying)::text, ('RELEASED_NO_ORDER'::character varying)::text])))
WITH DATA;

ALTER TABLE public.mv_stock_imbalance_by_facility_report
    OWNER TO postgres;

-- View: public.mv_dashboard_consumption_summary

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_consumption_summary;

CREATE MATERIALIZED VIEW public.mv_dashboard_consumption_summary
    TABLESPACE pg_default
AS
SELECT DISTINCT p.code AS productcode,
                p.id AS productid,
                r.programid,
                r.periodid,
                p.primaryname AS productname,
                pr.name AS periodname,
                CASE
                    WHEN (i.stockinhand = 0) THEN 'SO'::text
                    ELSE
                        CASE
                            WHEN ((i.amc > 0) AND (i.stockinhand > 0)) THEN
                                CASE
                                    WHEN (round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) < fap.minmonthsofstock) THEN 'US'::text
                                    WHEN ((round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) >= fap.minmonthsofstock) AND (round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) <= (fap.maxmonthsofstock)::numeric)) THEN 'SP'::text
                                    WHEN (round(((i.stockinhand)::numeric / (i.amc)::numeric), 2) > (fap.maxmonthsofstock)::numeric) THEN 'OS'::text
                                    ELSE NULL::text
                                    END
                            ELSE 'UK'::text
                            END
                    END AS status,
                CASE
                    WHEN (COALESCE(i.amc, 0) = 0) THEN (0)::numeric
                    ELSE trunc(round(((i.stockinhand)::numeric / (i.amc)::numeric), 2), 1)
                    END AS mos,
                i.amc,
                i.stockinhand,
                date_part('year'::text, pr.startdate) AS year,
                pr.scheduleid,
                i.quantitydispensed
FROM ((((((((products p
    JOIN program_products pp ON ((p.id = pp.productid)))
    JOIN programs ON ((pp.programid = programs.id)))
    JOIN requisitions r ON ((r.programid = pp.programid)))
    JOIN facilities f ON ((f.id = r.facilityid)))
    JOIN facility_types ft ON ((ft.id = f.typeid)))
    JOIN requisition_line_items i ON (((i.rnrid = r.id) AND ((i.productcode)::text = (p.code)::text))))
    JOIN processing_periods pr ON ((r.periodid = pr.id)))
         JOIN facility_approved_products fap ON (((ft.id = fap.facilitytypeid) AND (fap.programproductid = pp.id))))
WHERE ((COALESCE(i.stockinhand, 0) > 0) AND (r.emergency = false) AND (i.skipped = false))
WITH DATA;

ALTER TABLE public.mv_dashboard_consumption_summary
    OWNER TO postgres;


-- View: public.mv_dashboard_timeliness_report

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_timeliness_report;

CREATE MATERIALIZED VIEW public.mv_dashboard_timeliness_report
    TABLESPACE pg_default
AS
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
           WHEN (COALESCE(date_part('day'::text, (requisitions.createddate - ((pp.enddate)::date)::timestamp without time zone)), (0)::double precision) <= (COALESCE((( SELECT configuration_settings.value
                                                                                                                                                                         FROM configuration_settings
                                                                                                                                                                         WHERE ((configuration_settings.key)::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text)))::integer, 0))::double precision) THEN 'R'::text
           WHEN (COALESCE(date_part('day'::text, (requisitions.createddate - ((pp.enddate)::date)::timestamp without time zone)), (0)::double precision) > (COALESCE((( SELECT configuration_settings.value
                                                                                                                                                                        FROM configuration_settings
                                                                                                                                                                        WHERE ((configuration_settings.key)::text = 'UNSCHEDULED_REPORTING_CUT_OFF_DATE'::text)))::integer, 0))::double precision) THEN 'U'::text
           WHEN (COALESCE(date_part('day'::text, (requisitions.createddate - ((pp.enddate)::date)::timestamp without time zone)), (0)::double precision) > (COALESCE((( SELECT configuration_settings.value
                                                                                                                                                                        FROM configuration_settings
                                                                                                                                                                        WHERE ((configuration_settings.key)::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text)))::integer, 0))::double precision) THEN 'L'::text
           ELSE 'N'::text
           END AS reportingstatus,
       CASE
           WHEN (COALESCE(date_part('day'::text, (requisitions.createddate - ((pp.enddate)::date)::timestamp without time zone)), (0)::double precision) <= (COALESCE((( SELECT configuration_settings.value
                                                                                                                                                                         FROM configuration_settings
                                                                                                                                                                         WHERE ((configuration_settings.key)::text = 'REDESIGNED_DISTRICT_REPORTING_CUT_OFF_DATE'::text)))::integer, 0))::double precision) THEN 'R'::text
           WHEN (COALESCE(date_part('day'::text, (requisitions.createddate - ((pp.enddate)::date)::timestamp without time zone)), (0)::double precision) > (COALESCE((( SELECT configuration_settings.value
                                                                                                                                                                        FROM configuration_settings
                                                                                                                                                                        WHERE ((configuration_settings.key)::text = 'UNSCHEDULED_REPORTING_CUT_OFF_DATE'::text)))::integer, 0))::double precision) THEN 'U'::text
           WHEN (COALESCE(date_part('day'::text, (requisitions.createddate - ((pp.enddate)::date)::timestamp without time zone)), (0)::double precision) > (COALESCE((( SELECT configuration_settings.value
                                                                                                                                                                        FROM configuration_settings
                                                                                                                                                                        WHERE ((configuration_settings.key)::text = 'REDESIGNED_DISTRICT_REPORTING_CUT_OFF_DATE'::text)))::integer, 0))::double precision) THEN 'L'::text
           ELSE 'N'::text
           END AS redesignreportingstatus
FROM (((((((requisitions
    JOIN facilities ON ((requisitions.facilityid = facilities.id)))
    JOIN requisition_group_members rgm ON ((rgm.facilityid = requisitions.facilityid)))
    JOIN facility_types ON ((facilities.typeid = facility_types.id)))
    JOIN programs_supported ps ON (((ps.programid = requisitions.programid) AND (requisitions.facilityid = ps.facilityid))))
    JOIN processing_periods pp ON ((pp.id = requisitions.periodid)))
    JOIN requisition_group_program_schedules rgps ON (((rgps.requisitiongroupid = rgm.requisitiongroupid) AND (rgps.programid = requisitions.programid) AND (pp.scheduleid = rgps.scheduleid))))
         JOIN geographic_zones ON ((facilities.geographiczoneid = geographic_zones.id)))
WHERE (((requisitions.status)::text = ANY (ARRAY['RELEASED_NO_ORDER'::text, 'AUTHORIZED'::text, ('IN_APPROVAL'::character varying)::text, ('APPROVED'::character varying)::text, ('RELEASED'::character varying)::text])) AND (facilities.active = true) AND (requisitions.emergency = false))
GROUP BY requisitions.status, requisitions.createddate, pp.enddate, requisitions.id, requisitions.programid, requisitions.periodid, rgps.scheduleid, facilities.geographiczoneid, facilities.name, facilities.code, facilities.id, facility_types.id, facility_types.name
WITH DATA;

ALTER TABLE public.mv_dashboard_timeliness_report
    OWNER TO postgres;


-- View: public.mv_requisition_adjustment

-- DROP MATERIALIZED VIEW IF EXISTS public.mv_requisition_adjustment;

CREATE MATERIALIZED VIEW public.mv_requisition_adjustment
    TABLESPACE pg_default
AS
SELECT programs.id AS program_id,
       programs.name AS program_name,
       processing_periods.id AS processing_periods_id,
       processing_periods.name AS processing_periods_name,
       processing_periods.startdate AS processing_periods_start_date,
       processing_periods.enddate AS processing_periods_end_date,
       processing_schedules.id AS processing_schedules_id,
       processing_schedules.name AS processing_schedules_name,
       facility_types.name AS facility_type_name,
       facility_types.id AS facility_type_id,
       facilities.code AS facility_code,
       facilities.id AS facility_id,
       facilities.name AS facility_name,
       requisition_line_items.id AS requisition_line_item_id,
       requisition_line_items.productcode,
       requisition_line_items.product,
       products.id AS product_id,
       product_categories.name AS product_category_name,
       product_categories.id AS product_category_id,
       requisitions.status AS req_status,
       requisition_line_items.beginningbalance,
       requisition_line_items.quantityreceived,
       requisition_line_items.quantitydispensed,
       requisition_line_items.stockinhand,
       requisition_line_items.quantityrequested,
       requisition_line_items.calculatedorderquantity,
       requisition_line_items.quantityapproved,
       requisition_line_items.totallossesandadjustments,
       requisition_line_items.newpatientcount,
       requisition_line_items.stockoutdays,
       requisition_line_items.normalizedconsumption,
       requisition_line_items.amc,
       requisition_line_items.maxmonthsofstock,
       requisition_line_items.maxstockquantity,
       requisition_line_items.packstoship,
       requisition_line_items.packsize,
       requisition_line_items.fullsupply,
       requisition_line_item_losses_adjustments.type AS adjustment_type,
       requisition_line_item_losses_adjustments.quantity AS adjutment_qty,
       losses_adjustments_types.displayorder AS adjustment_display_order,
       losses_adjustments_types.additive AS adjustment_additive,
       (fn_get_supplying_facility_name(requisitions.supervisorynodeid))::text AS supplying_facility_name,
       requisition_line_items.id
FROM ((((((((((((requisition_line_items
    JOIN requisitions ON ((requisition_line_items.rnrid = requisitions.id)))
    JOIN products ON (((requisition_line_items.productcode)::text = (products.code)::text)))
    JOIN programs ON ((requisitions.programid = programs.id)))
    JOIN program_products ON (((products.id = program_products.productid) AND (program_products.programid = programs.id))))
    JOIN processing_periods ON ((requisitions.periodid = processing_periods.id)))
    JOIN product_categories ON ((program_products.productcategoryid = product_categories.id)))
    JOIN processing_schedules ON ((processing_periods.scheduleid = processing_schedules.id)))
    JOIN facilities ON ((requisitions.facilityid = facilities.id)))
    JOIN facility_types ON ((facilities.typeid = facility_types.id)))
    JOIN geographic_zones ON ((facilities.geographiczoneid = geographic_zones.id)))
    JOIN requisition_line_item_losses_adjustments ON ((requisition_line_items.id = requisition_line_item_losses_adjustments.requisitionlineitemid)))
         JOIN losses_adjustments_types ON (((requisition_line_item_losses_adjustments.type)::text = (losses_adjustments_types.name)::text)))
WITH NO DATA;

ALTER TABLE public.mv_requisition_adjustment
    OWNER TO postgres;


CREATE INDEX mv_requisition_adjustment_index_facility_code
    ON public.mv_requisition_adjustment USING btree
        (facility_code COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX mv_requisition_adjustment_index_id
    ON public.mv_requisition_adjustment USING btree
        (id)
    TABLESPACE pg_default;
CREATE INDEX mv_requisition_adjustment_index_productcode
    ON public.mv_requisition_adjustment USING btree
        (productcode COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX mv_requisition_adjustment_index_supplying_facility_name
    ON public.mv_requisition_adjustment USING btree
        (supplying_facility_name COLLATE pg_catalog."default")
    TABLESPACE pg_default;



