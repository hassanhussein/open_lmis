 DROP MATERIALIZED VIEW IF EXISTS public.vw_vaccine_monthly_vaccinations_by_dosage;

CREATE MATERIALIZED VIEW public.vw_vaccine_monthly_vaccinations_by_dosage AS
 SELECT vr.facilityid,
    vrcli.productid,
    vrcli.doseid,
    date_part('year'::text, pp.startdate) AS year,
    date_part('month'::text, pp.startdate) AS month,
    sum(coalesce(vrcli.regularmale,0) + coalesce(vrcli.regularfemale,0) + coalesce(vrcli.outreachmale,0) + coalesce(vrcli.outreachfemale,0)+ coalesce(vrcli.regularoutreachmale,0) +
    coalesce(vrcli.regularoutreachFemale,0) ) AS monthlyregular,
    pp.id AS periodid
   FROM vaccine_report_coverage_line_items vrcli
     JOIN vaccine_reports vr ON vr.id = vrcli.reportid
     JOIN processing_periods pp ON pp.id = vr.periodid
  GROUP BY vr.facilityid, (date_part('month'::text, pp.startdate)), (date_part('year'::text, pp.startdate)), vrcli.productid, vrcli.doseid, pp.startdate, pp.id
  ORDER BY vr.facilityid, (date_part('month'::text, pp.startdate))
WITH NO DATA;

ALTER TABLE public.vw_vaccine_monthly_vaccinations_by_dosage
  OWNER TO postgres;

-- Index: public.i_vw_vaccine_monthly_vaccination_by_dosage

-- DROP INDEX public.i_vw_vaccine_monthly_vaccination_by_dosage;

CREATE INDEX i_vw_vaccine_monthly_vaccination_by_dosage
  ON public.vw_vaccine_monthly_vaccinations_by_dosage
  USING btree
  (facilityid, productid, doseid, year, month);

