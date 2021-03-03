DROP FUNCTION IF EXISTS public.fn_new_get_timeliness_reporting_dates(integer, text, text);

CREATE OR REPLACE FUNCTION public.fn_new_get_timeliness_reporting_dates(
    IN in_periodid integer,
    IN cut_off_conf_key text,
    IN unschedule_cut_off_conf_key text)
  RETURNS TABLE(reportingstartdate date, reportingenddate date, reportinglatestartdate date, reportinglateenddate date) AS
$BODY$
BEGIN
RETURN QUERY EXECUTE
'SELECT CAST(date_trunc(''month'', enddate::date) + INTERVAL ''1 month'' as date) reportingStartDate,
(enddate::date + COALESCE((( SELECT configuration_settings.value FROM configuration_settings
WHERE configuration_settings.key::text = '''|| cut_off_conf_key || '''::text))::integer, 0)::integer ) reportingEndDate,
(CAST(date_trunc(''month'', enddate::date) + INTERVAL ''1 month'' as date) + COALESCE((( SELECT configuration_settings.value FROM configuration_settings
WHERE configuration_settings.key::text = '' || cut_off_conf_key || ''::text))::integer, 0)::integer ) lateReportingStartDate,
(enddate::date + COALESCE((( SELECT configuration_settings.value FROM configuration_settings
WHERE configuration_settings.key::text = ''' || unschedule_cut_off_conf_key || '''::text))::integer, 0)::integer ) lateReportingEndDate FROM processing_periods
WHERE   id = ' || in_periodid || '';
END
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION public.fn_new_get_timeliness_reporting_dates(integer, text, text)
    OWNER TO postgres;