DROP MATERIALIZED VIEW IF EXISTS public.mv_dashboard_tracer_product_by_program_counts;

CREATE MATERIALIZED VIEW public.mv_dashboard_tracer_product_by_program_counts AS
 SELECT pp.programid,
    programs.name,
    count(*) AS total
   FROM products p
     JOIN program_products pp ON p.id = pp.productid
     JOIN programs ON pp.programid = programs.id
  WHERE p.tracer = true
  GROUP BY pp.programid, programs.name
WITH DATA;

ALTER TABLE public.mv_dashboard_tracer_product_by_program_counts
  OWNER TO postgres;


