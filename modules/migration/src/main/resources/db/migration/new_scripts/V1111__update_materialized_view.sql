DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_tracer_available_by_programs;

CREATE MATERIALIZED VIEW public.mv_dashboard_tracer_available_by_programs AS
 SELECT DISTINCT i.productcode,
    programs.name AS program_name,
    r.programid,
    r.periodid,
    pr.name AS periodname
   FROM products p
     JOIN program_products pp ON p.id = pp.productid
     JOIN programs ON pp.programid = programs.id
     JOIN requisitions r ON r.programid = pp.programid
     JOIN requisition_line_items i ON i.rnrid = r.id AND i.productcode::text = p.code::text
     JOIN processing_periods pr ON r.periodid = pr.id
  WHERE p.tracer = true AND i.stockinhand > 0
WITH NO DATA;

ALTER TABLE public.mv_dashboard_tracer_available_by_programs
  OWNER TO postgres;

-- Index: public.i_mv_dashboard_tracer_available_by_programs_periodid

-- DROP INDEX public.i_mv_dashboard_tracer_available_by_programs_periodid;

CREATE INDEX i_mv_dashboard_tracer_available_by_programs_periodid
  ON public.mv_dashboard_tracer_available_by_programs
  USING btree
  (periodid);

-- Index: public.i_mv_dashboard_tracer_available_by_programs_productcode

-- DROP INDEX public.i_mv_dashboard_tracer_available_by_programs_productcode;

CREATE INDEX i_mv_dashboard_tracer_available_by_programs_productcode
  ON public.mv_dashboard_tracer_available_by_programs
  USING btree
  (productcode COLLATE pg_catalog."default");

-- Index: public.i_mv_dashboard_tracer_available_by_programs_programid

-- DROP INDEX public.i_mv_dashboard_tracer_available_by_programs_programid;

CREATE INDEX i_mv_dashboard_tracer_available_by_programs_programid
  ON public.mv_dashboard_tracer_available_by_programs
  USING btree
  (programid);

