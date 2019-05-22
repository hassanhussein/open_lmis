-- View: public.mv_order_fulfillment

 DROP MATERIALIZED VIEW if EXISTS public.mv_order_fulfillment;

CREATE MATERIALIZED VIEW  public.mv_order_fulfillment
TABLESPACE pg_default
AS
 SELECT shipment_line_items.orderid,
    sum(COALESCE(shipment_line_items.quantityshipped, 0)) AS quantityshipped,
    sum(COALESCE(shipment_line_items.substitutedproductquantityshipped, 0)) AS substitutedquantityshipped,
    shipment_line_items.productcode,
    shipment_line_items.shippeddate
   FROM shipment_line_items
  GROUP BY shipment_line_items.orderid, shipment_line_items.productcode, shipment_line_items.shippeddate
WITH no data;

ALTER TABLE public.mv_order_fulfillment
    OWNER TO postgres;