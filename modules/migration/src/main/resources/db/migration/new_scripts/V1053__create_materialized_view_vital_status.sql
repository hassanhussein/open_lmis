-- View: public.mv_vital_states_query

 DROP MATERIALIZED VIEW if exists public.mv_vital_states_query;

CREATE MATERIALIZED VIEW public.mv_vital_states_query
TABLESPACE pg_default
AS
 WITH product_age AS (
         SELECT p.id,
            date_part('month'::text, age(now()::timestamp without time zone, p.createddate)) + 12::double precision * date_part('year'::text, age(now()::timestamp without time zone, p.createddate)) AS months
           FROM products p
        ), facilities_age AS (
         SELECT f.id,
            date_part('month'::text, age(now()::timestamp without time zone, f.createddate)) + 12::double precision * date_part('year'::text, age(now()::timestamp without time zone, f.createddate)) AS months
           FROM facilities f
        ), program_age AS (
         SELECT pr.id,
            date_part('month'::text, age(now()::timestamp without time zone, pr.createddate)) + 12::double precision * date_part('year'::text, age(now()::timestamp without time zone, pr.createddate)) AS months
           FROM programs pr
        ), users_age AS (
         SELECT u.id,
            date_part('month'::text, age(now()::timestamp without time zone, u.createddate)) + 12::double precision * date_part('year'::text, age(now()::timestamp without time zone, u.createddate)) AS months
           FROM users u
        ), requistion_age AS (
         SELECT r.id,
            rsc.status,
            r.emergency,
            date_part('month'::text, age(now()::timestamp without time zone, r.createddate)) + 12::double precision * date_part('year'::text, age(now()::timestamp without time zone, r.createddate)) AS months
           FROM requisitions r
             JOIN requisition_status_changes rsc ON r.id = rsc.rnrid
        ), order_age AS (
         SELECT o.id,
            o.status,
            date_part('month'::text, age(now()::timestamp without time zone, o.createddate)) + 12::double precision * date_part('year'::text, age(now()::timestamp without time zone, o.createddate)) AS months
           FROM orders o
        )
 SELECT 'Number of Products'::text AS model,
    count(*) FILTER (WHERE pa.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE pa.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE pa.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM product_age pa
UNION
 SELECT 'Number of Facilities'::text AS model,
    count(*) FILTER (WHERE fa.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE fa.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE fa.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM facilities_age fa
UNION
 SELECT 'Number of Programs'::text AS model,
    count(*) FILTER (WHERE pra.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE pra.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE pra.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM program_age pra
UNION
 SELECT 'Number of Users'::text AS model,
    count(*) FILTER (WHERE u.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE u.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE u.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM users_age u
UNION
 SELECT 'Number of R&Rs submitted'::text AS model,
    count(*) FILTER (WHERE ra.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE ra.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE ra.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM requistion_age ra
  WHERE ra.status::text = 'SUBMITTED'::text
UNION
 SELECT 'Number of Emergency R&Rs'::text AS model,
    count(*) FILTER (WHERE ra.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE ra.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE ra.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM requistion_age ra
  WHERE ra.emergency = true
UNION
 SELECT 'Number of R&R Approved'::text AS model,
    count(*) FILTER (WHERE ra.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE ra.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE ra.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM requistion_age ra
  WHERE ra.status::text = 'APPROVED'::text
UNION
 SELECT 'Number of Orders Placed to MSL'::text AS model,
    count(*) FILTER (WHERE oa.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE oa.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE oa.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM order_age oa
UNION
 SELECT 'Number of Shipments setn by MSL'::text AS model,
    count(*) FILTER (WHERE oa.months = 0::double precision) AS current_month,
    count(*) FILTER (WHERE oa.months <= 3::double precision) AS last_three_month,
    count(*) FILTER (WHERE oa.months <= 6::double precision) AS last_six_month,
    count(*) AS total
   FROM order_age oa
  WHERE oa.status::text = 'RELEASED'::text
WITH NO DATA;

ALTER TABLE public.mv_vital_states_query
    OWNER TO postgres;