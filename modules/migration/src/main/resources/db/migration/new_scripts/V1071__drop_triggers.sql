
-- FUNCTION: public.trig_refresh_mv_order_full_fillment()
DROP TRIGGER if exists trig_refresh_mv_requisition ON public.requisition_line_items;
DROP FUNCTION if exists public.trig_refresh_mv_requisition();
--------------------------------------------------------------------
DROP TRIGGER if exists trig_refresh_mv_order_fullfillment ON
   public.shipment_line_items;
DROP FUNCTION if exists public.trig_refresh_mv_order_full_fillment();
-------------------------------------------------------------------------
