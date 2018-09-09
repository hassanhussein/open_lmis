-- View: public.mv_item_fill_rate

DROP MATERIALIZED VIEW if exists public.mv_item_fill_rate;

CREATE MATERIALIZED VIEW public.mv_item_fill_rate
TABLESPACE pg_default
AS
 WITH ifr AS (
         SELECT
	 p.fullname product,
	 r.programid,
            r.periodid,
            r.facilityid,
            l.productcode,
            r.id AS rnrid,
            l.quantityapproved,
            s.quantityshipped,
            round(s.quantityshipped::numeric / l.quantityapproved::numeric * 100::numeric, 1) AS item_fill_rate,
            l.quantityapproved - s.quantityshipped AS quantityinshort,
            round((l.quantityapproved::numeric - s.quantityshipped::numeric) / l.quantityapproved::numeric * 100::numeric, 1) AS short_supply_rate
           FROM requisition_line_items l
             JOIN requisitions r ON r.id = l.rnrid
             JOIN shipment_line_items s ON s.orderid = r.id AND s.productcode::text = l.productcode::text
          join products p on l.productcode =p.code
	 WHERE l.quantityapproved > 0
        )
 SELECT ifr.programid,
    ifr.periodid,
    ifr.facilityid,
    ifr.productcode,
	ifr.product,
    ifr.rnrid,
    ifr.quantityapproved,
    ifr.quantityshipped,
    ifr.item_fill_rate,
    ifr.quantityinshort,
    ifr.short_supply_rate
   FROM ifr
WITH DATA;

ALTER TABLE public.mv_item_fill_rate
    OWNER TO postgres;