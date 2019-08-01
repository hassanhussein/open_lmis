-- FUNCTION: public.trig_refresh_mv_order_full_fillment()
DROP TRIGGER if exists trig_refresh_mv_requisition ON public.requisition_line_items;
DROP FUNCTION if exists public.trig_refresh_mv_requisition();
----------------------------------------------------------------------
CREATE FUNCTION public.trig_refresh_mv_requisition()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$

BEGIN
    REFRESH MATERIALIZED VIEW mv_requisition;
    RETURN NULL;
END;

$BODY$;

ALTER FUNCTION public.trig_refresh_mv_requisition()
    OWNER TO postgres;
	-------------------------------
	CREATE TRIGGER trig_refresh_mv_requisition
    AFTER INSERT OR DELETE OR TRUNCATE OR UPDATE 
    ON public.requisition_line_items
    FOR EACH STATEMENT
    EXECUTE PROCEDURE public.trig_refresh_mv_requisition();
