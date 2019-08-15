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

