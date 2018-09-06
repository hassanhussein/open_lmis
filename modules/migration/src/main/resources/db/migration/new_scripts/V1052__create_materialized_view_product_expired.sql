-- View: public.mv_expired_tracer_products

DROP MATERIALIZED VIEW if exists public.mv_expired_tracer_products;

CREATE MATERIALIZED VIEW public.mv_expired_tracer_products
TABLESPACE pg_default
AS
 WITH prod AS (
         SELECT p.id,
            p.code,
            p.fullname
           FROM products p
          WHERE p.tracer = true

        ), expired AS (
         SELECT r.programid,
            r.periodid,
            rl.productcode,
            sum(COALESCE(rl.stockinhand, 0)) AS balance,
            sum(COALESCE(ia.quantity, 0)) AS expired_quantity,
                CASE
                    WHEN sum(COALESCE(rl.stockinhand, 0)) > 0 THEN sum(COALESCE(ia.quantity, 0))::numeric / sum(COALESCE(rl.stockinhand, 0))::numeric * 100::numeric
                    ELSE 0::numeric
                END AS percentage
           FROM requisitions r
             JOIN requisition_line_items rl ON rl.rnrid = r.id
             JOIN requisition_line_item_losses_adjustments ia ON ia.requisitionlineitemid = rl.id
             JOIN losses_adjustments_types t ON t.name::text = ia.type::text
             JOIN prod p ON p.code::text = rl.productcode::text
          WHERE t.name::text = 'EXPIRED'::text
          GROUP BY r.programid, r.periodid, rl.productcode
        )
 SELECT expired.programid,
    expired.periodid,
    expired.productcode,
    expired.balance,
    expired.expired_quantity,
    expired.percentage
   FROM expired
WITH NO DATA;

ALTER TABLE public.mv_expired_tracer_products
    OWNER TO postgres;