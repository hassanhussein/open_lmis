--Delete from vaccine_product_targets where productid in (2417,2429);
--INSERT INTO vaccine_product_targets (productid,targetwastagegood, targetwastagewarn, targetwastagebad, targetcoveragebad, targetcoveragewarn, targetcoveragegood, targetdropoutgood, targetdropoutwarn, targetdropoutbad, targetwastageclosedvialsgood, targetwastageclosedvialswarn, targetwastageclosedvialsbad) VALUES ('2417', '5', '5', '5', '50', '80', '90', '5', '10', '20', '5', '10', '20');
--INSERT INTO vaccine_product_targets (productid,targetwastagegood, targetwastagewarn, targetwastagebad, targetcoveragebad, targetcoveragewarn, targetcoveragegood, targetdropoutgood, targetdropoutwarn, targetdropoutbad, targetwastageclosedvialsgood, targetwastageclosedvialswarn, targetwastageclosedvialsbad) VALUES ('2429', '5', '5', '5', '50', '80', '90', '5', '10', '20', '5', '10', '20');

-- View: vw_vaccine_coverage


DROP VIEW if exists vw_vaccine_coverage CASCADE;

CREATE OR REPLACE VIEW vw_vaccine_coverage AS
SELECT a.program_id,
a.geographic_zone_id,
a.geographic_zone_name,
a.level_id,
a.parent_id,
a.period_id,
a.period_name,
a.period_year,
a.period_start_date,
a.period_end_date,
a.facility_id,
a.facility_code,
a.facility_name,
a.report_id,
a.fixed_immunization_session,
a.outreach_immunization_session,
a.product_id,
a.product_code,
a.product_name,
a.dose_id,
a.display_order,
a.display_name,
a.within_male,
a.within_female,
a.within_total,
0 AS within_coverage,
a.outside_male,
a.outside_female,
a.outside_total,
0 AS outside_coverage,
a.camp_male,
a.camp_female,
a.camp_total,
a.within_outside_total,
fn_get_vaccine_coverage_denominator(a.program_id, a.facility_id, a.period_year, a.product_id, a.dose_id) AS denominator,
--1 AS denominator,
a.cum_within_total,
a.cum_outside_total,
a.cum_within_total + a.cum_outside_total AS cum_within_outside_total,
0 AS within_outside_coverage,
0 AS cum_within_coverage,
0 AS cum_outside_coverage,
0 AS cum_within_outside_coverage,
a.bcg_1,
a.mr_1,
a.dtp_1,
a.dtp_3
FROM ( WITH temp AS (
SELECT geographic_zones.id AS geographic_zone_id,
geographic_zones.name AS geographic_zone_name,
geographic_zones.levelid AS level_id,
geographic_zones.parentid AS parent_id,
processing_periods.id AS period_id,
processing_periods.name AS period_name,
processing_periods.startdate AS period_start_date,
processing_periods.enddate AS period_end_date,
facilities.id AS facility_id,
facilities.code AS facility_code,
facilities.name AS facility_name,
vaccine_reports.id AS report_id,
vaccine_reports.programid AS program_id,
vaccine_reports.fixedimmunizationsessions AS fixed_immunization_session,
vaccine_reports.outreachimmunizationsessions AS outreach_immunization_session,
products.id AS product_id,
products.code AS product_code,
products.primaryname AS product_name,
vaccine_report_coverage_line_items.doseid AS dose_id,
program_products.displayorder AS display_order,
vaccine_report_coverage_line_items.displayname AS display_name,
vaccine_report_coverage_line_items.regularmale AS within_male,
vaccine_report_coverage_line_items.regularfemale AS within_female,
vaccine_report_coverage_line_items.regularoutreachmale AS within_outreach_male,
vaccine_report_coverage_line_items.regularoutreachfemale AS within_outreach_female,
vaccine_report_coverage_line_items.outreachmale AS outside_male,
vaccine_report_coverage_line_items.outreachfemale AS outside_female,
vaccine_report_coverage_line_items.campaignmale AS camp_male,
vaccine_report_coverage_line_items.campaignfemale AS camp_female,
0 AS cum_within_total,
0 AS cum_outside_total,
0 AS bcg_1,
0 AS mr_1,
0 AS dtp_1,
0 AS dtp_3
FROM vaccine_report_coverage_line_items
JOIN vaccine_reports ON vaccine_report_coverage_line_items.reportid = vaccine_reports.id
JOIN processing_periods ON vaccine_reports.periodid = processing_periods.id
JOIN facilities ON vaccine_reports.facilityid = facilities.id
JOIN geographic_zones ON facilities.geographiczoneid = geographic_zones.id
JOIN products ON vaccine_report_coverage_line_items.productid = products.id
JOIN program_products ON products.id = program_products.productid AND program_products.programid = vaccine_reports.programid
WHERE program_products.active = true
)
SELECT b.program_id,
b.geographic_zone_id,
b.geographic_zone_name,
b.level_id,
b.parent_id,
b.period_id,
b.period_name,
date_part('year'::text, b.period_start_date)::integer AS period_year,
b.period_start_date,
b.period_end_date,
b.facility_id,
b.facility_code,
b.facility_name,
b.report_id,
b.fixed_immunization_session,
b.outreach_immunization_session,
b.product_id,
b.product_code,
b.product_name,
b.dose_id,
b.display_order,
b.display_name,
COALESCE(b.within_male, 0) AS within_male,
COALESCE(b.within_female, 0) AS within_female,
COALESCE(b.within_male, 0) + COALESCE(b.within_female, 0) AS within_total,
COALESCE(b.within_male, 0) AS within_outreach_male,
COALESCE(b.within_female, 0) AS within_outreach_female,
COALESCE(b.within_outreach_male, 0) + COALESCE(b.within_outreach_female, 0) AS within_outreach_total,
COALESCE(b.outside_male, 0) AS outside_male,
COALESCE(b.outside_female, 0) AS outside_female,
COALESCE(b.outside_male, 0) + COALESCE(b.outside_female, 0) AS outside_total,
COALESCE(b.camp_male, 0) AS camp_male,
COALESCE(b.camp_female, 0) AS camp_female,
COALESCE(b.camp_male, 0) + COALESCE(b.camp_female, 0) AS camp_total,
COALESCE(b.within_male, 0) + COALESCE(b.within_female, 0) + COALESCE(b.outside_male, 0) + COALESCE(b.outside_female, 0) + COALESCE(b.within_outreach_male, 0) + COALESCE(b.within_outreach_female, 0) AS within_outside_total,
b.cum_within_total,
b.cum_outside_total,
b.bcg_1,
b.mr_1,
b.dtp_1,
b.dtp_3
FROM temp b) a;
--ORDER BY a.display_order;

ALTER TABLE vw_vaccine_coverage
OWNER TO postgres;


DROP VIEW IF EXISTS public.vw_vims_dhis_female_integrations;

CREATE OR REPLACE VIEW public.vw_vims_dhis_female_integrations AS
( SELECT vw_vaccine_coverage.facility_code,
    vw_vaccine_coverage.product_code AS productcode,
    vw_vaccine_coverage.product_name,
    vw_vaccine_coverage.dose_id AS doseid,
    to_char(vw_vaccine_coverage.period_start_date, 'YYYYMM'::text) AS period,
    sum(vw_vaccine_coverage.within_male) AS datavalues,
    'WF'::text AS status,
    ((((vw_vaccine_coverage.product_code::text || ''::text) || vw_vaccine_coverage.product_name::text) || ''::text) || 'WF'::text) || vw_vaccine_coverage.dose_id AS vims_code
   FROM vw_vaccine_coverage
  WHERE vw_vaccine_coverage.program_id = 82
  GROUP BY vw_vaccine_coverage.product_code, vw_vaccine_coverage.product_name, vw_vaccine_coverage.dose_id, vw_vaccine_coverage.facility_code, vw_vaccine_coverage.period_start_date
  ORDER BY vw_vaccine_coverage.dose_id)
UNION
 SELECT vw_vaccine_coverage.facility_code,
    vw_vaccine_coverage.product_code AS productcode,
    vw_vaccine_coverage.product_name,
    vw_vaccine_coverage.dose_id AS doseid,
    to_char(vw_vaccine_coverage.period_start_date, 'YYYYMM'::text) AS period,
    sum(vw_vaccine_coverage.outside_male) AS datavalues,
    'OF'::text AS status,
    ((((vw_vaccine_coverage.product_code::text || ''::text) || vw_vaccine_coverage.product_name::text) || ''::text) || 'OF'::text) || vw_vaccine_coverage.dose_id AS vims_code
   FROM vw_vaccine_coverage
  WHERE vw_vaccine_coverage.program_id = 82
  GROUP BY vw_vaccine_coverage.product_code, vw_vaccine_coverage.product_name, vw_vaccine_coverage.dose_id, vw_vaccine_coverage.facility_code, vw_vaccine_coverage.period_start_date;

ALTER TABLE public.vw_vims_dhis_female_integrations
  OWNER TO postgres;



DROP VIEW IF EXISTS public.vw_vims_dhis_male_integrations;

CREATE OR REPLACE VIEW public.vw_vims_dhis_male_integrations AS
( SELECT vw_vaccine_coverage.facility_code,
    vw_vaccine_coverage.product_code AS productcode,
    vw_vaccine_coverage.product_name,
    vw_vaccine_coverage.dose_id AS doseid,
    to_char(vw_vaccine_coverage.period_start_date, 'YYYYMM'::text) AS period,
    sum(vw_vaccine_coverage.within_male) AS datavalues,
    'WM'::text AS status,
    ((((vw_vaccine_coverage.product_code::text || ''::text) || vw_vaccine_coverage.product_name::text) || ''::text) || 'WM'::text) || vw_vaccine_coverage.dose_id AS vims_code
   FROM vw_vaccine_coverage
  WHERE vw_vaccine_coverage.program_id = 82
  GROUP BY vw_vaccine_coverage.product_code, vw_vaccine_coverage.product_name, vw_vaccine_coverage.dose_id, vw_vaccine_coverage.facility_code, vw_vaccine_coverage.period_start_date
  ORDER BY vw_vaccine_coverage.dose_id)
UNION
 SELECT vw_vaccine_coverage.facility_code,
    vw_vaccine_coverage.product_code AS productcode,
    vw_vaccine_coverage.product_name,
    vw_vaccine_coverage.dose_id AS doseid,
    to_char(vw_vaccine_coverage.period_start_date, 'YYYYMM'::text) AS period,
    sum(vw_vaccine_coverage.outside_male) AS datavalues,
    'OM'::text AS status,
    ((((vw_vaccine_coverage.product_code::text || ''::text) || vw_vaccine_coverage.product_name::text) || ''::text) || 'OM'::text) || vw_vaccine_coverage.dose_id AS vims_code
   FROM vw_vaccine_coverage
  WHERE vw_vaccine_coverage.program_id = 82
  GROUP BY vw_vaccine_coverage.product_code, vw_vaccine_coverage.product_name, vw_vaccine_coverage.dose_id, vw_vaccine_coverage.facility_code, vw_vaccine_coverage.period_start_date;

ALTER TABLE public.vw_vims_dhis_male_integrations
  OWNER TO postgres;
