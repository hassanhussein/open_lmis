
 DROP FUNCTION if exists public.refresh_views();

CREATE OR REPLACE FUNCTION public.refresh_views()
  RETURNS character varying AS
$BODY$
DECLARE msg character varying(2000);
BEGIN
msg := 'Procedure completed successfully.';
REFRESH MATERIALIZED VIEW mv_dashboard_consumption_summary;
RETURN msg;
EXCEPTION WHEN OTHERS THEN
return SQLERRM;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.refresh_views()
  OWNER TO postgres;