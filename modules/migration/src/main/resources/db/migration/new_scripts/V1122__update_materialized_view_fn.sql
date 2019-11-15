 DROP FUNCTION if exists public.refresh_view_via_api(schema_arg text);

CREATE OR REPLACE FUNCTION public.refresh_view_via_api(schema_arg text)
  RETURNS character varying AS
$BODY$
DECLARE msg character varying(2000);
BEGIN
msg := 'Procedure completed successfully.';

EXECUTE 'REFRESH MATERIALIZED VIEW ' || schema_arg;

RETURN msg;
EXCEPTION WHEN OTHERS THEN
return SQLERRM;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.refresh_view_via_api(m_view text)
  OWNER TO postgres;