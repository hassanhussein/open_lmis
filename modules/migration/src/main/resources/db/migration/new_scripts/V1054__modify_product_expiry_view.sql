DROP MATERIALIZED VIEW IF EXISTS public.mv_expired_tracer_products;

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
            p.fullname AS product,
            f.id AS facilityid,
            f.name AS facility,
			pp.name as period,
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
             JOIN facilities f ON r.facilityid = f.id
		     JOIN processing_periods pp on pp.id=r.periodid
          WHERE t.name::text = 'EXPIRED'::text
          GROUP BY r.programid, r.periodid, rl.productcode, p.fullname, f.id, f.name,pp.name
        )
 SELECT expired.programid,
    expired.periodid,
    expired.productcode,
    expired.balance,
    expired.expired_quantity,
    expired.product,
    expired.facility,
    expired.facilityid,
    expired.percentage,
	expired.period
   FROM expired
WITH DATA;

ALTER TABLE public.mv_expired_tracer_products
    OWNER TO postgres;