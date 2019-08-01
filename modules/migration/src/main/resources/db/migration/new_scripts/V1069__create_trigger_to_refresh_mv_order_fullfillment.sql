DROP TRIGGER if exists trig_refresh_mv_order_fullfillment ON
   public.shipment_line_items;
DROP FUNCTION if exists public.trig_refresh_mv_order_full_fillment();
--------------------------------------------------------------------------------

CREATE FUNCTION public.trig_refresh_mv_order_full_fillment()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$

BEGIN
    REFRESH MATERIALIZED VIEW mv_order_fulfillment;
    RETURN NULL;
END;

$BODY$;
--------------------------------------------------------------

CREATE TRIGGER trig_refresh_mv_order_fullfillment
    AFTER INSERT OR DELETE OR TRUNCATE OR UPDATE
    ON public.shipment_line_items
    FOR EACH STATEMENT
    EXECUTE PROCEDURE public.trig_refresh_mv_order_full_fillment();