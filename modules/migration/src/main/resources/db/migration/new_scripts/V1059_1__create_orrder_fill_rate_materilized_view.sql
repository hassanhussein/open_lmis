-- View: public.mv_order_fill_report_products

DROP MATERIALIZED VIEW if exists public.mv_order_fill_report_products;

CREATE MATERIALIZED VIEW public.mv_order_fill_report_products
TABLESPACE pg_default
AS
 SELECT d.district_name AS district,
    d.district_id AS districtid,
    d.region_id AS provinceid,
    d.region_name AS province,
    f.name AS facility,
    pr.name AS program,
    pr.id AS programid,
    pp.id AS periodid,
    pp.name AS period,
    f.code AS facilitycode,
	p.id as productid,
	p.tracer as tracer,
    li.productcode,
    li.product,
    li.quantityrequested AS "order",
    li.quantityapproved AS approved,
    li.rnrid,
    sli.quantityshipped AS receipts,
    sli.substitutedproductcode,
    sli.substitutedproductname,
    sli.substitutedproductquantityshipped,
        CASE
            WHEN COALESCE(li.quantityapproved::numeric, 0::numeric) = 0::numeric THEN 0::numeric
            ELSE round(COALESCE(sli.quantityshipped, 0::bigint)::numeric / COALESCE(li.quantityapproved, 0)::numeric * 100::numeric, 2)
        END AS item_fill_rate
   FROM requisitions r
     JOIN requisition_line_items li ON r.id = li.rnrid
     JOIN processing_periods pp ON r.periodid = pp.id
     JOIN programs pr ON pr.id = r.programid
     JOIN products p ON li.productcode::text = p.code::text
     JOIN facilities f ON f.id = r.facilityid
     JOIN vw_districts d ON f.geographiczoneid = d.district_id
     LEFT JOIN ( SELECT DISTINCT substitutes.productcode,
            substitutes.orderid,
            substitutes.quantityshipped,
            substitutes.substitutedproductcode,
            substitutes.substitutedproductname,
            substitutes.substitutedproductquantityshipped
           FROM ( SELECT NULL::integer AS orderid,
                    NULL::bigint AS quantityshipped,
                    li_1.productcode,
                    li_1.substitutedproductcode,
                    li_1.substitutedproductname,
                    li_1.substitutedproductquantityshipped
                   FROM shipment_line_items li_1
                  WHERE li_1.substitutedproductcode IS NOT NULL
                UNION
                 SELECT shipment_line_items.orderid,
                    sum(shipment_line_items.quantityshipped) AS quantityshipped,
                    shipment_line_items.productcode,
                    NULL::character varying AS substitutedproductcode,
                    NULL::character varying AS substitutedproductname,
                    NULL::integer AS substitutedproductquantityshipped
                   FROM shipment_line_items
                  GROUP BY shipment_line_items.orderid, shipment_line_items.productcode) substitutes
          ORDER BY substitutes.productcode, substitutes.substitutedproductcode DESC) sli ON sli.productcode::text = li.productcode::text AND li.rnrid = sli.orderid
  WHERE r.status::text = 'RELEASED'::text AND li.quantityapproved > 0
  ORDER BY f.name, li.productcode
WITH  no DATA;

ALTER TABLE public.mv_order_fill_report_products
    OWNER TO postgres;