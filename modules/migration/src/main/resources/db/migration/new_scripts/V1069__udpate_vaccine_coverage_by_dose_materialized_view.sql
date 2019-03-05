--BEGIN;
--ALTER TABLE vaccine_report_coverage_line_items DISABLE TRIGGER ALL;
-- now the RI over table b is disabled


DROP MATERIALIZED VIEW IF EXISTS public.vw_vaccine_classification_by_districts;

drop view if exists vw_vaccine_wastage_rate_by_district;
drop view if exists vw_vaccine_cumulative_coverage_by_district;
drop view if exists vw_vaccine_cumulative_coverage;
DROP MATERIALIZED VIEW IF EXISTS public.vw_vaccine_facility_classification;
DROP MATERIALIZED VIEW IF EXISTS public.facility_classification_view;
drop view if exists vw_vaccine_cumulative_consumption;
drop view if exists vw_vaccine_cumulative_consumption_by_district;
drop MATERIALIZED view if exists vw_vaccine_monthly_consumption;
drop MATERIALIZED view if exists vw_vaccine_monthly_vaccinations;
DROP INDEX IF EXISTS i_vw_vaccine_monthly_vaccination_by_dosage;

DROP materialized view IF EXISTS categorization_view ;
DROP VIEW IF EXISTS vw_penta_dropout_district_summary;
DROP VIEW IF EXISTS vw_vaccine_district_penta_dropouts;

DROP VIEW IF EXISTS  vw_vaccine_coverage_by_dose_and_district;
DROP MATERIALIZED VIEW IF EXISTS public.facility__classification_categorization_view;
  DROP MATERIALIZED VIEW IF EXISTS VW_PRODUCT_COVERAGE;
DROP VIEW IF EXISTS vw_vaccine_cumulative_coverage_by_dose;
DROP VIEW IF EXISTS vw_penta_dropout_facility_summary;
DROP VIEW IF EXISTS vw_vaccine_facility_penta_dropouts;
DROP VIEW IF EXISTS vw_district_bcg_mr_dropout_summary;
DROP VIEW IF EXISTS vw_district_bcg_mr_dropout;
DROP VIEW IF EXISTS vw_facility_bcg_mr_dropout;
DROP VIEW IF Exists x_vw_vaccine_cumulative_coverage_by_dose;
DROP MATERIALIZED VIEW IF EXISTS vw_vaccine_monthly_vaccinations_by_dosage;

-- First Drop
--Vaccine Rate By District

CREATE MATERIALIZED VIEW vw_vaccine_monthly_consumption
  AS
    SELECT
      vr.facilityid,
      vrcli.productid,
      extract(YEAR FROM pp.startdate)                   AS year,
      extract(MONTH FROM pp.startdate)                  AS month,
      sum(coalesce(vrcli.quantityissued, 0))            AS monthlyConsumption,
      sum(coalesce(vrcli.quantitydiscardedunopened, 0)) AS monthlyDiscardedUnopnened,
      pp.id                                             AS periodid
    FROM vaccine_report_logistics_line_items vrcli
      JOIN vaccine_reports vr ON vr.id = vrcli.reportid
      JOIN processing_periods pp ON pp.id = vr.periodid
    GROUP BY vr.facilityid, extract(MONTH FROM pp.startdate), extract(YEAR FROM pp.startdate), vrcli.productid,
      pp.startDate, pp.id
    ORDER BY facilityid, extract(MONTH FROM pp.startdate);


CREATE INDEX i_vw_monthly_vaccine_consumption
  ON vw_vaccine_monthly_consumption (facilityid, productid, year, month);



CREATE OR REPLACE VIEW vw_vaccine_cumulative_consumption_by_district AS
  SELECT
    av.year,
    av.month,
    ff.geographiczoneid,
    sum(av.monthlyconsumption)             AS monthlyconsumption,
    av.productid,
    av.periodid,
    (SELECT sum(a.monthlyconsumption) AS sum
     FROM (vw_vaccine_monthly_consumption a
       JOIN facilities f ON ((f.id = a.facilityid)))
     WHERE ((f.geographiczoneid = ff.geographiczoneid) AND (a.year = av.year) AND (a.month <= av.month) AND
            (a.productid = av.productid))) AS cumulative
  FROM (vw_vaccine_monthly_consumption av
    JOIN facilities ff ON ((ff.id = av.facilityid)))
  GROUP BY av.productid, av.periodid, av.year, av.month, ff.geographiczoneid;


--MOnthly Vaccinated goes here
drop MATERIALIZED view if exists vw_vaccine_monthly_consumptions;
CREATE MATERIALIZED VIEW vw_vaccine_monthly_vaccinations
  AS
    SELECT
      vr.facilityid,
      vrcli.productid,
      extract(YEAR FROM pp.startdate)              AS year,
      extract(MONTH FROM pp.startdate)             AS month,
          sum(coalesce(vrcli.regularmale,0) + coalesce(vrcli.regularfemale,0) + coalesce(vrcli.outreachmale,0) + coalesce(vrcli.outreachfemale,0)+ coalesce(vrcli.regularoutreachmale,0) +
          coalesce(vrcli.regularoutreachFemale,0) ) AS monthlyregular,
      pp.id                                        AS periodid
    FROM vaccine_report_coverage_line_items vrcli
      JOIN vaccine_reports vr ON vr.id = vrcli.reportid
      JOIN processing_periods pp ON pp.id = vr.periodid
    GROUP BY vr.facilityid, extract(MONTH FROM pp.startdate), extract(YEAR FROM pp.startdate), vrcli.productid,
      pp.startDate, pp.id
    ORDER BY facilityid, extract(MONTH FROM pp.startdate);


CREATE OR REPLACE VIEW vw_vaccine_cumulative_coverage AS
  SELECT
    a.facilityid,
    a.facilityname,
    a.year,
    a.month,
    a.monthlyregular,
    a.productid,
    a.estimate,
    a.periodid,
    a.coveragepercentage,
    a.cumulative,
    CASE
    WHEN ((a.estimate IS NOT NULL) AND (a.estimate <> 0))
      THEN (((a.cumulative * (100) :: NUMERIC)) :: DOUBLE PRECISION / (a.month * (a.estimate) :: DOUBLE PRECISION))
    ELSE NULL :: DOUBLE PRECISION
    END AS cumulativepercentage
  FROM (SELECT
          av.facilityid,
          si.name                                  AS facilityname,
          av.year,
          av.month,
          av.monthlyregular,
          av.productid,
          si.estimate,
          av.periodid,
          CASE
          WHEN ((si.estimate IS NOT NULL) AND (si.estimate <> 0))
            THEN ((av.monthlyregular * 100) / si.estimate)
          ELSE NULL :: BIGINT
          END                                      AS coveragepercentage,
          (SELECT sum(a_1.monthlyregular) AS sum
           FROM vw_vaccine_monthly_vaccinations a_1
           WHERE ((a_1.facilityid = av.facilityid) AND (a_1.year = av.year) AND (a_1.month <= av.month) AND
                  (a_1.productid = av.productid))) AS cumulative
        FROM ((vw_vaccine_monthly_vaccinations av
          JOIN vw_monthly_estimate si ON ((si.facilityid = av.facilityid)))
          JOIN (SELECT DISTINCT
                  vaccine_product_doses.productid,
                  max(vaccine_product_doses.denominatorestimatecategoryid) AS demographicestimateid
                FROM vaccine_product_doses
                GROUP BY vaccine_product_doses.productid) pp ON ((pp.productid = av.productid)))
        WHERE (((si.year) :: DOUBLE PRECISION = av.year) AND (si.demographicestimateid = pp.demographicestimateid))) a;


CREATE OR REPLACE VIEW vw_vaccine_cumulative_coverage_by_district AS
  SELECT
    f.geographiczoneid,
    coverage.periodid,
    coverage.productid,
    coverage.year,
    coverage.month,
    pp.name                      AS period_name,
    sum(coverage.monthlyregular) AS monthlyregular,
    sum(coverage.cumulative)     AS cumulativecoverage
  FROM ((vw_vaccine_cumulative_coverage coverage
    JOIN processing_periods pp ON ((pp.id = coverage.periodid)))
    JOIN facilities f ON ((f.id = coverage.facilityid)))
  GROUP BY f.geographiczoneid, coverage.periodid, coverage.productid, coverage.year, pp.name, coverage.month;


  CREATE OR REPLACE VIEW vw_vaccine_wastage_rate_by_district AS
  SELECT
    consumption.periodid,
    consumption.month,
    coverage.period_name,
    consumption.year,
    consumption.geographiczoneid,
    consumption.productid,
    CASE
    WHEN ((consumption.monthlyconsumption IS NOT NULL) AND (consumption.monthlyconsumption <> (0) :: NUMERIC))
      THEN (((consumption.monthlyconsumption - coverage.monthlyregular) * (100) :: NUMERIC) /
            consumption.monthlyconsumption)
    ELSE NULL :: NUMERIC
    END AS wastagerate
  FROM (vw_vaccine_cumulative_consumption_by_district consumption
    JOIN vw_vaccine_cumulative_coverage_by_district coverage
      ON (((consumption.productid = coverage.productid) AND (consumption.geographiczoneid = coverage.geographiczoneid)
           AND (consumption.periodid = coverage.periodid))));

CREATE MATERIALIZED VIEW vw_vaccine_monthly_vaccinations_by_dosage
  AS
    SELECT
      vr.facilityid,
      vrcli.productid,
      vrcli.doseid,
      extract(YEAR FROM pp.startdate)              AS year,
      extract(MONTH FROM pp.startdate)             AS month,
          sum(coalesce(vrcli.regularmale,0) + coalesce(vrcli.regularfemale,0) + coalesce(vrcli.outreachmale,0) + coalesce(vrcli.outreachfemale,0)+ coalesce(vrcli.regularoutreachmale,0) +
          coalesce(vrcli.regularoutreachFemale,0) ) AS monthlyregular,
      pp.id                                        AS periodid
    FROM vaccine_report_coverage_line_items vrcli
      JOIN vaccine_reports vr ON vr.id = vrcli.reportid
      JOIN processing_periods pp ON pp.id = vr.periodid
    GROUP BY vr.facilityid, extract(MONTH FROM pp.startdate), extract(YEAR FROM pp.startdate), vrcli.productid,
      vrcli.doseid, pp.startDate, pp.id
    ORDER BY facilityid, extract(MONTH FROM pp.startdate);


--CREATE INDEX i_vw_vaccine_monthly_vaccination_by_dosage
  --ON vw_vaccine_monthly_vaccinations_by_dosage (facilityid, productid, doseid, year, month);


CREATE OR REPLACE VIEW vw_vaccine_cumulative_coverage_by_dose AS
  SELECT
    a.facilityid,
    a.facilityname,
    a.year,
    a.month,
    a.monthlyregular,
    a.productid,
    a.estimate,
    a.doseid,
    a.periodid,
    a.denominatorestimatecategoryid,
    a.coveragepercentage,
    a.cumulative,
    CASE
    WHEN ((a.estimate IS NOT NULL) AND (a.estimate <> 0))
      THEN (((a.cumulative * (100) :: NUMERIC)) :: DOUBLE PRECISION / (a.month * (a.estimate) :: DOUBLE PRECISION))
    ELSE NULL :: DOUBLE PRECISION
    END AS cumulativepercentage
  FROM (SELECT
          av.facilityid,
          si.name                                                               AS facilityname,
          av.year,
          av.month,
          av.monthlyregular,
          av.productid,
          si.estimate,
          av.doseid,
          av.periodid,
          vd.denominatorestimatecategoryid,
          CASE
          WHEN ((si.estimate IS NOT NULL) AND (si.estimate <> 0))
            THEN ((av.monthlyregular * 100) / si.estimate)
          ELSE NULL :: BIGINT
          END                                                                   AS coveragepercentage,
          (SELECT sum(a_1.monthlyregular) AS sum
           FROM vw_vaccine_monthly_vaccinations_by_dosage a_1
           WHERE ((a_1.facilityid = av.facilityid) AND (a_1.year = av.year) AND (a_1.month <= av.month) AND
                  (a_1.productid = av.productid) AND (a_1.doseid = av.doseid))) AS cumulative
        FROM ((vw_vaccine_monthly_vaccinations_by_dosage av
          JOIN vaccine_product_doses vd ON (((vd.productid = av.productid) AND (vd.doseid = av.doseid))))
          JOIN vw_monthly_estimate si
            ON (((si.facilityid = av.facilityid) AND (si.demographicestimateid = vd.denominatorestimatecategoryid))))
        WHERE (((si.year) :: DOUBLE PRECISION = av.year) AND
               (si.demographicestimateid = vd.denominatorestimatecategoryid))) a;

CREATE OR REPLACE VIEW vw_vaccine_coverage_by_dose_and_district AS
  SELECT
    a.geographiczoneid,
    a.doseid,
    a.productid,
    a.year,
    a.month,
    a.periodid,
    a.monthlyestimate,
    a.cumulativemonthlyregular,
    a.coveragepercentage,
    (((p.primaryname) :: TEXT || ' - ' :: TEXT) || a.doseid) AS product_dose,
    d.district_name,
    d.district_id,
    d.region_name,
    CASE
    WHEN (a.coveragepercentage IS NULL)
      THEN NULL :: TEXT
    WHEN ((a.coveragepercentage) :: DOUBLE PRECISION > pt.targetcoveragegood)
      THEN 'good' :: TEXT
    WHEN ((a.coveragepercentage) :: DOUBLE PRECISION > pt.targetcoveragewarn)
      THEN 'normal' :: TEXT
    WHEN ((a.coveragepercentage) :: DOUBLE PRECISION > pt.targetcoveragebad)
      THEN 'warn' :: TEXT
    ELSE 'bad' :: TEXT
    END                                                      AS coverageclassification
  FROM ((((SELECT
             f.geographiczoneid,
             d_1.doseid,
             d_1.productid,
             d_1.year,
             d_1.month,
             d_1.periodid,
             e.monthlyestimate,
             sum(d_1.monthlyregular) AS cumulativemonthlyregular,
             CASE
             WHEN ((e.monthlyestimate IS NOT NULL) AND (e.monthlyestimate <> 0))
               THEN ((sum(d_1.monthlyregular) * (100) :: NUMERIC) / (e.monthlyestimate) :: NUMERIC)
             ELSE NULL :: NUMERIC
             END                     AS coveragepercentage
           FROM ((vw_vaccine_cumulative_coverage_by_dose d_1
             JOIN facilities f ON ((f.id = d_1.facilityid)))
             JOIN vw_monthly_district_estimate e
               ON (((e.districtid = f.geographiczoneid) AND (d_1.year = (e.year) :: DOUBLE PRECISION) AND
                    (d_1.denominatorestimatecategoryid = e.demographicestimateid))))
           GROUP BY d_1.year, d_1.month, d_1.periodid, f.geographiczoneid, d_1.productid, d_1.doseid,
             e.monthlyestimate) a
    JOIN vw_districts d ON ((d.district_id = a.geographiczoneid)))
    JOIN products p ON ((p.id = a.productid)))
    JOIN vaccine_product_targets pt ON ((pt.productid = a.productid)));


CREATE MATERIALIZED VIEW public.vw_vaccine_classification_by_districts AS
 SELECT b.wastageclassification,
    b.periodid,
    b.month,
    b.period_name,
    b.year,
    b.geographiczoneid,
    b.productid,
    b.wastagerate,
    b.district_id,
    b.district_name,
    b.region_id,
    b.region_name,
    b.zone_id,
    b.zone_name,
    b.parent,
        CASE
            WHEN b.wastageclassification = 'good'::text AND coverage.coverageclassification = 'good'::text THEN 'Class A'::text
            WHEN b.wastageclassification <> 'good'::text AND coverage.coverageclassification = 'good'::text THEN 'Class B'::text
            WHEN b.wastageclassification = 'good'::text AND coverage.coverageclassification <> 'good'::text THEN 'Class C'::text
            ELSE 'Class D'::text
        END AS classification,
        CASE
            WHEN b.wastageclassification = 'good'::text AND coverage.coverageclassification = 'good'::text THEN 'good'::text
            WHEN b.wastageclassification <> 'good'::text AND coverage.coverageclassification = 'good'::text THEN 'normal'::text
            WHEN b.wastageclassification = 'good'::text AND coverage.coverageclassification <> 'good'::text THEN 'warn'::text
            ELSE 'bad'::text
        END AS classificationclass,
    b.periodid AS period_id
   FROM ( SELECT
                CASE
                    WHEN w.wastagerate::double precision < t.targetwastagegood THEN 'good'::text
                    WHEN w.wastagerate::double precision < t.targetcoveragewarn THEN 'normal'::text
                    WHEN w.wastagerate::double precision < t.targetcoveragebad THEN 'warn'::text
                    WHEN w.wastagerate::double precision > t.targetcoveragebad THEN 'bad'::text
                    ELSE NULL::text
                END AS wastageclassification,
            w.periodid,
            w.month,
            w.period_name,
            w.year,
            w.geographiczoneid,
            w.productid,
            w.wastagerate,
            d.district_id,
            d.district_name,
            d.region_id,
            d.region_name,
            d.zone_id,
            d.zone_name,
            d.parent
           FROM vw_vaccine_wastage_rate_by_district w
             JOIN vw_districts d ON d.district_id = w.geographiczoneid
             JOIN vaccine_product_targets t ON w.productid = t.productid) b
     JOIN ( SELECT dos.geographiczoneid,
            dos.doseid,
            dos.productid,
            dos.year,
            dos.month,
            dos.periodid,
            dos.monthlyestimate,
            dos.cumulativemonthlyregular,
            dos.coveragepercentage,
            dos.product_dose,
            dos.district_name,
            dos.district_id,
            dos.region_name,
            dos.coverageclassification
           FROM vw_vaccine_coverage_by_dose_and_district dos
          WHERE dos.doseid = (( SELECT max(vaccine_product_doses.doseid) AS max
                   FROM vaccine_product_doses
                  WHERE dos.productid = vaccine_product_doses.productid))) coverage ON coverage.geographiczoneid = b.geographiczoneid AND coverage.productid = b.productid AND coverage.periodid = b.periodid
     JOIN processing_periods pp ON b.periodid = pp.id AND pp.numberofmonths = 1 AND pp.scheduleid = 45
WITH NO DATA;

--vw_vaccine_cumulative_consumptio goes here


CREATE OR REPLACE VIEW vw_vaccine_cumulative_consumption AS
  SELECT
    av.facilityid,
    av.year,
    av.month,
    av.monthlyconsumption,
    av.productid,
    av.periodid,
    (SELECT sum(a.monthlyconsumption) AS sum
     FROM vw_vaccine_monthly_consumption a
     WHERE ((a.facilityid = av.facilityid) AND (a.year = av.year) AND (a.month <= av.month) AND
            (a.productid = av.productid))) AS cumulative
  FROM vw_vaccine_monthly_consumption av;


CREATE MATERIALIZED VIEW public.vw_vaccine_facility_classification AS
 SELECT b.facilityid,
    b.month,
    b.year,
    b.product_id,
    b.periodid,
    b.cumulativeconsumption,
    b.monthlyconsumption,
    b.monthlyregular,
    b.doseid,
    b.cumulativecoverage,
    b.cumulativepercentage,
    b.facility_name,
    b.district_name,
    b.district_id,
    b.coverageclassification,
    b.id,
    b.productid,
    b.targetwastagegood,
    b.targetwastagewarn,
    b.targetwastagebad,
    b.targetcoveragebad,
    b.targetcoveragewarn,
    b.targetcoveragegood,
    b.targetdropoutgood,
    b.targetdropoutwarn,
    b.targetdropoutbad,
    b.targetwastageclosedvialsgood,
    b.targetwastageclosedvialswarn,
    b.targetwastageclosedvialsbad,
    b.wastage,
    b.period,
    b.percentagewasted,
    b.wastageclassification,
        CASE
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification = 'good'::text THEN 'Class A'::text
            WHEN b.wastageclassification <> 'good'::text AND b.coverageclassification = 'good'::text THEN 'Class B'::text
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification <> 'good'::text THEN 'Class C'::text
            ELSE 'Class D'::text
        END AS classification,
        CASE
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification = 'good'::text THEN 'good'::text
            WHEN b.wastageclassification <> 'good'::text AND b.coverageclassification = 'good'::text THEN 'normal'::text
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification <> 'good'::text THEN 'warn'::text
            ELSE 'bad'::text
        END AS classificationclass,
    pp.name AS period_name
   FROM ( SELECT a.facilityid,
            a.month,
            a.year,
            a.product_id,
            a.periodid,
            a.cumulativeconsumption,
            a.monthlyconsumption,
            a.monthlyregular,
            a.doseid,
            a.cumulativecoverage,
            a.cumulativepercentage,
            a.facility_name,
            a.district_name,
            a.district_id,
            a.coverageclassification,
            a.id,
            a.productid,
            a.targetwastagegood,
            a.targetwastagewarn,
            a.targetwastagebad,
            a.targetcoveragebad,
            a.targetcoveragewarn,
            a.targetcoveragegood,
            a.targetdropoutgood,
            a.targetdropoutwarn,
            a.targetdropoutbad,
            a.targetwastageclosedvialsgood,
            a.targetwastageclosedvialswarn,
            a.targetwastageclosedvialsbad,
            a.wastage,
            a.period,
            a.percentagewasted,
                CASE
                    WHEN a.percentagewasted::double precision < a.targetwastagegood THEN 'good'::text
                    WHEN a.percentagewasted::double precision < a.targetcoveragewarn THEN 'normal'::text
                    WHEN a.percentagewasted::double precision < a.targetcoveragebad THEN 'warn'::text
                    ELSE 'bad'::text
                END AS wastageclassification
           FROM ( SELECT consumption.facilityid,
                    consumption.month,
                    consumption.year,
                    consumption.productid AS product_id,
                    consumption.periodid,
                    consumption.cumulative AS cumulativeconsumption,
                    consumption.monthlyconsumption,
                    vaccinated.monthlyregular,
                    vaccinated.doseid,
                    vaccinated.cumulative AS cumulativecoverage,
                    vaccinated.cumulativepercentage,
                    f_1.name AS facility_name,
                    d_1.district_name,
                    d_1.district_id,
                        CASE
                            WHEN vaccinated.coveragepercentage::double precision > target.targetcoveragegood THEN 'good'::text
                            WHEN vaccinated.coveragepercentage::double precision > target.targetcoveragewarn THEN 'normal'::text
                            WHEN vaccinated.coveragepercentage::double precision > target.targetcoveragebad THEN 'warn'::text
                            ELSE 'bad'::text
                        END AS coverageclassification,
                    target.id,
                    target.productid,
                    target.targetwastagegood,
                    target.targetwastagewarn,
                    target.targetwastagebad,
                    target.targetcoveragebad,
                    target.targetcoveragewarn,
                    target.targetcoveragegood,
                    target.targetdropoutgood,
                    target.targetdropoutwarn,
                    target.targetdropoutbad,
                    target.targetwastageclosedvialsgood,
                    target.targetwastageclosedvialswarn,
                    target.targetwastageclosedvialsbad,
                    consumption.cumulative - vaccinated.cumulative AS wastage,
                    pp_1.name AS period,
                        CASE
                            WHEN consumption.cumulative IS NOT NULL AND consumption.cumulative <> 0::numeric THEN 100::numeric * (consumption.cumulative - vaccinated.cumulative) / consumption.cumulative
                            ELSE NULL::numeric
                        END AS percentagewasted
                   FROM vw_vaccine_cumulative_consumption consumption
                     JOIN vw_vaccine_cumulative_coverage_by_dose vaccinated ON consumption.productid = vaccinated.productid AND consumption.facilityid = vaccinated.facilityid AND consumption.month = vaccinated.month AND consumption.year = vaccinated.year
                     JOIN processing_periods pp_1 ON pp_1.id = vaccinated.periodid
                     JOIN facilities f_1 ON f_1.id = vaccinated.facilityid
                     JOIN vw_districts d_1 ON d_1.district_id = f_1.geographiczoneid
                     JOIN vaccine_product_targets target ON target.productid = consumption.productid
                  WHERE vaccinated.doseid = (( SELECT max(vd.doseid) AS max
                           FROM vaccine_product_doses vd
                          WHERE vd.productid = vaccinated.productid))
                  ORDER BY d_1.district_name, f_1.name) a) b
     JOIN facilities f ON f.id = b.facilityid
     JOIN processing_periods pp ON pp.id = b.periodid
     JOIN vw_districts d ON d.district_id = f.geographiczoneid
  WHERE pp.scheduleid = 45 AND pp.numberofmonths = 1
WITH NO DATA;

ALTER TABLE public.vw_vaccine_facility_classification
  OWNER TO postgres;

--stared here


CREATE OR REPLACE VIEW vw_vaccine_district_penta_dropouts AS
  SELECT
    f.geographiczoneid AS districtid,
    d.productid,
    d.doseid,
    d.year,
    d.month,
    d.periodid,
    CASE
    WHEN ((sum(d.monthlyregular) IS NOT NULL) AND (sum(d.monthlyregular) <> (0) :: NUMERIC))
      THEN (((sum(d.monthlyregular) - sum(dd.monthlyregular)) * (100) :: NUMERIC) / sum(d.monthlyregular))
    ELSE NULL :: NUMERIC
    END                AS dropout
  FROM ((vw_vaccine_monthly_vaccinations_by_dosage d
    JOIN (SELECT
            v.facilityid,
            v.productid,
            v.doseid,
            v.year,
            v.month,
            v.monthlyregular,
            v.periodid
          FROM vw_vaccine_monthly_vaccinations_by_dosage v
          WHERE (v.doseid = 3)) dd
      ON (((dd.month = d.month) AND (dd.year = d.year) AND (d.facilityid = dd.facilityid) AND
           (d.productid = dd.productid))))
    JOIN facilities f ON ((f.id = d.facilityid)))
  WHERE ((d.productid = 2421) AND (d.doseid = 1))
  GROUP BY f.geographiczoneid, d.productid, d.doseid, d.year, d.month, d.periodid;



CREATE OR REPLACE VIEW vw_penta_dropout_district_summary AS
  SELECT
    a.districtid,
    a.productid,
    a.doseid,
    a.year,
    a.month,
    a.periodid,
    a.dropout,
    pp.name AS period_name,
    d.district_id,
    d.district_name,
    d.region_id,
    d.region_name,
    d.zone_id,
    d.zone_name,
    d.parent,
    CASE
    WHEN (a.dropout <= (pt.targetdropoutgood) :: NUMERIC)
      THEN 'good' :: TEXT
    WHEN ((a.dropout > (pt.targetdropoutgood) :: NUMERIC) AND (a.dropout <= (pt.targetdropoutwarn) :: NUMERIC))
      THEN 'normal' :: TEXT
    WHEN ((a.dropout > (pt.targetdropoutwarn) :: NUMERIC) AND (a.dropout <= (pt.targetdropoutbad) :: NUMERIC))
      THEN 'warn' :: TEXT
    WHEN (a.dropout IS NOT NULL)
      THEN 'bad' :: TEXT
    ELSE NULL :: TEXT
    END     AS classification
  FROM (((vw_vaccine_district_penta_dropouts a
    JOIN processing_periods pp ON ((pp.id = a.periodid)))
    JOIN vw_districts d ON ((d.district_id = a.districtid)))
    JOIN vaccine_product_targets pt ON ((pt.productid = a.productid)));

CREATE OR REPLACE VIEW vw_penta_dropout_district_summary AS
  SELECT
    a.districtid,
    a.productid,
    a.doseid,
    a.year,
    a.month,
    a.periodid,
    a.dropout,
    pp.name AS period_name,
    d.district_id,
    d.district_name,
    d.region_id,
    d.region_name,
    d.zone_id,
    d.zone_name,
    d.parent,
    CASE
    WHEN (a.dropout <= (pt.targetdropoutgood) :: NUMERIC)
      THEN 'good' :: TEXT
    WHEN ((a.dropout > (pt.targetdropoutgood) :: NUMERIC) AND (a.dropout <= (pt.targetdropoutwarn) :: NUMERIC))
      THEN 'normal' :: TEXT
    WHEN ((a.dropout > (pt.targetdropoutwarn) :: NUMERIC) AND (a.dropout <= (pt.targetdropoutbad) :: NUMERIC))
      THEN 'warn' :: TEXT
    WHEN (a.dropout IS NOT NULL)
      THEN 'bad' :: TEXT
    ELSE NULL :: TEXT
    END     AS classification
  FROM (((vw_vaccine_district_penta_dropouts a
    JOIN processing_periods pp ON ((pp.id = a.periodid)))
    JOIN vw_districts d ON ((d.district_id = a.districtid)))
    JOIN vaccine_product_targets pt ON ((pt.productid = a.productid)));


CREATE MATERIALIZED VIEW public.categorization_view AS
 SELECT
        CASE
            WHEN pd.dropout::double precision <= pt.targetdropoutgood AND cc.coveragepercentage::double precision >= pt.targetcoveragegood THEN 'good'::text
            WHEN pd.dropout::double precision > pt.targetdropoutgood AND cc.coveragepercentage::double precision >= pt.targetcoveragegood THEN 'normal'::text
            WHEN pd.dropout::double precision <= pt.targetdropoutgood AND cc.coveragepercentage::double precision <= pt.targetcoveragegood THEN 'warn'::text
            ELSE 'bad'::text
        END AS classificationclass,
        CASE
            WHEN pd.dropout::double precision <= pt.targetdropoutgood AND cc.coveragepercentage::double precision >= pt.targetcoveragegood THEN 'Cat_1'::text
            WHEN pd.dropout::double precision > pt.targetdropoutgood AND cc.coveragepercentage::double precision >= pt.targetcoveragegood THEN 'Cat_2'::text
            WHEN pd.dropout::double precision <= pt.targetdropoutgood AND cc.coveragepercentage::double precision <= pt.targetcoveragegood THEN 'Cat_3'::text
            ELSE 'Cat_4'::text
        END AS catagorization,
    cc.month,
    cc.year,
    pd.district_id,
    d.region_id,
    cc.region_name,
    cc.periodid,
    pd.period_name,
    pd.district_name,
    pd.doseid
   FROM vw_vaccine_coverage_by_dose_and_district cc
     JOIN vw_penta_dropout_district_summary pd ON cc.doseid = pd.doseid AND pd.productid = cc.productid AND pd.district_id = cc.district_id AND pd.year = cc.year AND pd.month = cc.month
     JOIN vw_districts d ON d.district_id = cc.geographiczoneid
     JOIN vaccine_product_targets pt ON pt.productid = cc.productid
WITH NO DATA;


--End Here


CREATE MATERIALIZED VIEW public.facility_classification_view AS
 SELECT b.facilityid,
    b.month,
    b.year,
    b.product_id,
    b.period_id,
    b.cumulativeconsumption,
    b.monthlyconsumption,
    b.monthlyregular,
    b.doseid,
    b.cumulativecoverage,
    b.cumulativepercentage,
    b.facility_name,
    b.district_name,
    b.coverageclassification,
    b.id,
    b.productid,
    b.targetwastagegood,
    b.targetwastagewarn,
    b.targetwastagebad,
    b.targetcoveragebad,
    b.targetcoveragewarn,
    b.targetcoveragegood,
    b.targetdropoutgood,
    b.targetdropoutwarn,
    b.targetdropoutbad,
    b.targetwastageclosedvialsgood,
    b.targetwastageclosedvialswarn,
    b.targetwastageclosedvialsbad,
    b.wastage,
    b.period,
    b.percentagewasted,
    b.wastageclassification,
        CASE
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification = 'good'::text THEN 'Class A'::text
            WHEN b.wastageclassification <> 'good'::text AND b.coverageclassification = 'good'::text THEN 'Class B'::text
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification <> 'good'::text THEN 'Class C'::text
            ELSE 'Class D'::text
        END AS classification,
        CASE
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification = 'good'::text THEN 'good'::text
            WHEN b.wastageclassification <> 'good'::text AND b.coverageclassification = 'good'::text THEN 'normal'::text
            WHEN b.wastageclassification = 'good'::text AND b.coverageclassification <> 'good'::text THEN 'warn'::text
            ELSE 'bad'::text
        END AS classificationclass,
    f.name AS facilityname,
    d.district_name AS district,
    pp.name AS period_name,
    f.id AS facility_id,
    d.district_id
   FROM ( SELECT a.facilityid,
            a.month,
            a.year,
            a.product_id,
            a.period_id,
            a.cumulativeconsumption,
            a.monthlyconsumption,
            a.monthlyregular,
            a.doseid,
            a.cumulativecoverage,
            a.cumulativepercentage,
            a.facility_name,
            a.district_name,
            a.coverageclassification,
            a.id,
            a.productid,
            a.targetwastagegood,
            a.targetwastagewarn,
            a.targetwastagebad,
            a.targetcoveragebad,
            a.targetcoveragewarn,
            a.targetcoveragegood,
            a.targetdropoutgood,
            a.targetdropoutwarn,
            a.targetdropoutbad,
            a.targetwastageclosedvialsgood,
            a.targetwastageclosedvialswarn,
            a.targetwastageclosedvialsbad,
            a.wastage,
            a.period,
            a.percentagewasted,
                CASE
                    WHEN a.percentagewasted::double precision < a.targetwastagegood THEN 'good'::text
                    WHEN a.percentagewasted::double precision < a.targetcoveragewarn THEN 'normal'::text
                    WHEN a.percentagewasted::double precision < a.targetcoveragebad THEN 'warn'::text
                    ELSE 'bad'::text
                END AS wastageclassification
           FROM ( SELECT consumption.facilityid,
                    consumption.month,
                    consumption.year,
                    consumption.productid AS product_id,
                    consumption.periodid AS period_id,
                    consumption.cumulative AS cumulativeconsumption,
                    consumption.monthlyconsumption,
                    vaccinated.monthlyregular,
                    vaccinated.doseid,
                    vaccinated.cumulative AS cumulativecoverage,
                    vaccinated.cumulativepercentage,
                    f_1.name AS facility_name,
                    d_1.district_name,
                        CASE
                            WHEN vaccinated.coveragepercentage::double precision > target.targetcoveragegood THEN 'good'::text
                            WHEN vaccinated.coveragepercentage::double precision > target.targetcoveragewarn THEN 'normal'::text
                            WHEN vaccinated.coveragepercentage::double precision > target.targetcoveragebad THEN 'warn'::text
                            ELSE 'bad'::text
                        END AS coverageclassification,
                    target.id,
                    target.productid,
                    target.targetwastagegood,
                    target.targetwastagewarn,
                    target.targetwastagebad,
                    target.targetcoveragebad,
                    target.targetcoveragewarn,
                    target.targetcoveragegood,
                    target.targetdropoutgood,
                    target.targetdropoutwarn,
                    target.targetdropoutbad,
                    target.targetwastageclosedvialsgood,
                    target.targetwastageclosedvialswarn,
                    target.targetwastageclosedvialsbad,
                    consumption.cumulative - vaccinated.cumulative AS wastage,
                    pp_1.name AS period,
                        CASE
                            WHEN consumption.cumulative IS NOT NULL AND consumption.cumulative <> 0::numeric THEN 100::numeric * (consumption.cumulative - vaccinated.cumulative) / consumption.cumulative
                            ELSE NULL::numeric
                        END AS percentagewasted
                   FROM vw_vaccine_cumulative_consumption consumption
                     JOIN vw_vaccine_cumulative_coverage_by_dose vaccinated ON consumption.productid = vaccinated.productid AND consumption.facilityid = vaccinated.facilityid AND consumption.month = vaccinated.month AND consumption.year = vaccinated.year
                     JOIN processing_periods pp_1 ON pp_1.id = vaccinated.periodid
                     JOIN facilities f_1 ON f_1.id = vaccinated.facilityid
                     JOIN vw_districts d_1 ON d_1.district_id = f_1.geographiczoneid
                     JOIN vaccine_product_targets target ON target.productid = consumption.productid
                  WHERE vaccinated.doseid = (( SELECT max(vd.doseid) AS max
                           FROM vaccine_product_doses vd
                          WHERE vd.productid = vaccinated.productid))
                  ORDER BY d_1.district_name, f_1.name) a) b
     JOIN facilities f ON f.id = b.facilityid
     JOIN processing_periods pp ON pp.id = b.period_id
     JOIN vw_districts d ON d.district_id = f.geographiczoneid
WITH NO DATA;

ALTER TABLE public.facility_classification_view
  OWNER TO postgres;



CREATE INDEX i_vw_vaccine_monthly_vaccination
  ON vw_vaccine_monthly_vaccinations (facilityid, year, month);

----------------   End  --------------------


CREATE OR REPLACE VIEW vw_vaccine_facility_penta_dropouts AS
  SELECT
    d.facilityid,
    d.productid,
    d.doseid,
    d.year,
    d.month,
    d.periodid,
    CASE
    WHEN ((d.monthlyregular IS NOT NULL) AND (d.monthlyregular <> 0))
      THEN (((d.monthlyregular - dd.monthlyregular) * 100) / d.monthlyregular)
    ELSE NULL :: BIGINT
    END AS dropout
  FROM (vw_vaccine_monthly_vaccinations_by_dosage d
    JOIN (SELECT
            v.facilityid,
            v.productid,
            v.doseid,
            v.year,
            v.month,
            v.monthlyregular,
            v.periodid
          FROM vw_vaccine_monthly_vaccinations_by_dosage v
          WHERE (v.doseid = 3)) dd
      ON (((dd.month = d.month) AND (dd.year = d.year) AND (d.facilityid = dd.facilityid) AND
           (d.productid = dd.productid))))
  WHERE ((d.productid = 2421) AND (d.doseid = 1))
  GROUP BY d.facilityid, d.productid, d.doseid, d.monthlyregular, dd.monthlyregular, d.year, d.month, d.periodid;



----------------   End  --------------------


CREATE VIEW vw_district_bcg_mr_dropout AS
  SELECT
    f.geographiczoneid AS districtid,
    d.productid,
    d.doseid,
    d.year,
    d.month,
    d.periodid,
    CASE
    WHEN ((sum(d.monthlyregular) IS NOT NULL) AND (sum(d.monthlyregular) <> (0) :: NUMERIC))
      THEN ((sum(d.monthlyregular) - (max(dd.monthlyregular) * (100) :: NUMERIC)) / sum(d.monthlyregular))
    ELSE NULL :: NUMERIC
    END                AS dropout
  FROM ((vw_vaccine_monthly_vaccinations_by_dosage d
    JOIN facilities f ON ((f.id = d.facilityid)))
    LEFT JOIN (SELECT
                 ff.geographiczoneid,
                 sum(v.monthlyregular) AS monthlyregular,
                 v.month,
                 v.year,
                 v.productid,
                 v.doseid
               FROM (vw_vaccine_monthly_vaccinations_by_dosage v
                 JOIN facilities ff ON ((ff.id = v.facilityid)))
               WHERE ((v.productid = 2420) AND (v.doseid = 1))
               GROUP BY v.month, v.year, v.productid, v.doseid, ff.geographiczoneid) dd
      ON (((dd.month = d.month) AND (dd.year = d.year) AND (f.geographiczoneid = dd.geographiczoneid))))
  WHERE ((d.productid = 2412) AND (d.doseid = 1))
  GROUP BY f.geographiczoneid, d.productid, d.doseid, d.year, d.month, d.periodid;

CREATE VIEW vw_district_bcg_mr_dropout_summary AS
  SELECT
    a.districtid,
    a.productid,
    a.doseid,
    a.year,
    a.month,
    a.periodid,
    a.dropout,
    d.district_id,
    d.district_name,
    d.region_id,
    d.region_name,
    d.zone_id,
    d.zone_name,
    d.parent,
    pp.name AS period_name,
    CASE
    WHEN (a.dropout <= (pt.targetdropoutgood) :: NUMERIC)
      THEN 'good' :: TEXT
    WHEN ((a.dropout > (pt.targetdropoutgood) :: NUMERIC) AND (a.dropout <= (pt.targetdropoutwarn) :: NUMERIC))
      THEN 'normal' :: TEXT
    WHEN ((a.dropout > (pt.targetdropoutwarn) :: NUMERIC) AND (a.dropout <= (pt.targetdropoutbad) :: NUMERIC))
      THEN 'warn' :: TEXT
    WHEN (a.dropout IS NOT NULL)
      THEN 'bad' :: TEXT
    ELSE NULL :: TEXT
    END     AS classification
  FROM (((vw_district_bcg_mr_dropout a
    JOIN processing_periods pp ON ((pp.id = a.periodid)))
    JOIN vw_districts d ON ((d.district_id = a.districtid)))
    JOIN vaccine_product_targets pt ON ((pt.productid = a.productid)));

CREATE VIEW vw_facility_bcg_mr_dropout AS
  SELECT
    d.facilityid,
    d.productid,
    d.doseid,
    d.year,
    d.month,
    d.periodid,
    CASE
    WHEN ((d.monthlyregular IS NOT NULL) AND (d.monthlyregular <> 0))
      THEN (((d.monthlyregular - dd.monthlyregular) * 100) / d.monthlyregular)
    ELSE NULL :: BIGINT
    END AS dropout
  FROM (vw_vaccine_monthly_vaccinations_by_dosage d
    JOIN (SELECT
            v.facilityid,
            v.productid,
            v.doseid,
            v.year,
            v.month,
            v.monthlyregular,
            v.periodid
          FROM vw_vaccine_monthly_vaccinations_by_dosage v
          WHERE ((v.productid = 2420) AND (v.doseid = 1))) dd
      ON (((dd.month = d.month) AND (dd.year = d.year) AND (d.facilityid = dd.facilityid))))
  WHERE ((d.productid = 2412) AND (d.doseid = 1))
  GROUP BY d.facilityid, d.productid, d.doseid, d.monthlyregular, dd.monthlyregular, d.year, d.month, d.periodid;

CREATE OR REPLACE VIEW vw_monthly_district_estimate AS
  SELECT
    fde.districtid,
    fde.year,
    fde.demographicestimateid,
    (COALESCE(fde.value, 0) / 12) AS monthlyestimate,
    COALESCE(fde.value, 0)        AS annualestimate
  FROM district_demographic_estimates fde
  WHERE (fde.programid = 82);

CREATE OR REPLACE VIEW vw_monthly_estimate AS
  SELECT
    f.id                          AS facilityid,
    f.name,
    fde.year,
    fde.demographicestimateid,
    (COALESCE(fde.value, 0) / 12) AS estimate
  FROM (facilities f
    JOIN facility_demographic_estimates fde ON (((fde.facilityid = f.id) AND (fde.programid = 82))));

CREATE OR REPLACE VIEW vw_penta_dropout_facility_summary AS
  SELECT
    a.facilityid,
    a.productid,
    a.doseid,
    a.year,
    a.month,
    a.periodid,
    a.dropout,
    pp.name AS period_name,
    f.id    AS facility_id,
    f.name  AS facility_name,
    d.district_id,
    d.district_name,
    d.region_id,
    d.region_name,
    d.zone_id,
    d.zone_name,
    d.parent,
    CASE
    WHEN ((a.dropout) :: NUMERIC <= (pt.targetdropoutgood) :: NUMERIC)
      THEN 'good' :: TEXT
    WHEN (((a.dropout) :: NUMERIC > (pt.targetdropoutgood) :: NUMERIC) AND
          ((a.dropout) :: NUMERIC <= (pt.targetdropoutwarn) :: NUMERIC))
      THEN 'normal' :: TEXT
    WHEN (((a.dropout) :: NUMERIC > (pt.targetdropoutwarn) :: NUMERIC) AND
          ((a.dropout) :: NUMERIC <= (pt.targetdropoutbad) :: NUMERIC))
      THEN 'warn' :: TEXT
    WHEN (a.dropout IS NOT NULL)
      THEN 'bad' :: TEXT
    ELSE NULL :: TEXT
    END     AS classification
  FROM ((((vw_vaccine_facility_penta_dropouts a
    JOIN facilities f ON ((f.id = a.facilityid)))
    JOIN processing_periods pp ON ((pp.id = a.periodid)))
    JOIN vw_districts d ON ((d.district_id = f.geographiczoneid)))
    JOIN vaccine_product_targets pt ON ((pt.productid = a.productid)));

---and_district_


CREATE OR REPLACE VIEW vw_vaccine_coverage_by_dose_and_district AS
  SELECT
    a.geographiczoneid,
    a.doseid,
    a.productid,
    a.year,
    a.month,
    a.periodid,
    a.monthlyestimate,
    a.cumulativemonthlyregular,
    a.coveragepercentage,
    (((p.primaryname) :: TEXT || ' - ' :: TEXT) || a.doseid) AS product_dose,
    d.district_name,
    d.district_id,
    d.region_name,
    CASE
    WHEN (a.coveragepercentage IS NULL)
      THEN NULL :: TEXT
    WHEN ((a.coveragepercentage) :: DOUBLE PRECISION > pt.targetcoveragegood)
      THEN 'good' :: TEXT
    WHEN ((a.coveragepercentage) :: DOUBLE PRECISION > pt.targetcoveragewarn)
      THEN 'normal' :: TEXT
    WHEN ((a.coveragepercentage) :: DOUBLE PRECISION > pt.targetcoveragebad)
      THEN 'warn' :: TEXT
    ELSE 'bad' :: TEXT
    END                                                      AS coverageclassification
  FROM ((((SELECT
             f.geographiczoneid,
             d_1.doseid,
             d_1.productid,
             d_1.year,
             d_1.month,
             d_1.periodid,
             e.monthlyestimate,
             sum(d_1.monthlyregular) AS cumulativemonthlyregular,
             CASE
             WHEN ((e.monthlyestimate IS NOT NULL) AND (e.monthlyestimate <> 0))
               THEN ((sum(d_1.monthlyregular) * (100) :: NUMERIC) / (e.monthlyestimate) :: NUMERIC)
             ELSE NULL :: NUMERIC
             END                     AS coveragepercentage
           FROM ((vw_vaccine_cumulative_coverage_by_dose d_1
             JOIN facilities f ON ((f.id = d_1.facilityid)))
             JOIN vw_monthly_district_estimate e
               ON (((e.districtid = f.geographiczoneid) AND (d_1.year = (e.year) :: DOUBLE PRECISION) AND
                    (d_1.denominatorestimatecategoryid = e.demographicestimateid))))
           GROUP BY d_1.year, d_1.month, d_1.periodid, f.geographiczoneid, d_1.productid, d_1.doseid,
             e.monthlyestimate) a
    JOIN vw_districts d ON ((d.district_id = a.geographiczoneid)))
    JOIN products p ON ((p.id = a.productid)))
    JOIN vaccine_product_targets pt ON ((pt.productid = a.productid)));

-- Add dependency

CREATE MATERIALIZED VIEW public.facility__classification_categorization_view AS
 WITH q AS (
         SELECT
                CASE
                    WHEN pd.dropout <= 10 AND cc.coveragepercentage >= 90 THEN 'good'::text
                    WHEN pd.dropout > 10 AND cc.coveragepercentage >= 90 THEN 'normal'::text
                    WHEN pd.dropout <= 10 AND cc.coveragepercentage <= 90 THEN 'warn'::text
                    ELSE 'bad'::text
                END AS classificationclass,
                CASE
                    WHEN pd.dropout <= 10 AND cc.coveragepercentage >= 90 THEN 'Cat_1'::text
                    WHEN pd.dropout > 10 AND cc.coveragepercentage >= 90 THEN 'Cat_2'::text
                    WHEN pd.dropout <= 10 AND cc.coveragepercentage <= 90 THEN 'Cat_3'::text
                    ELSE 'Cat_4'::text
                END AS classification,
            cc.month,
            cc.year,
            pd.facilityid AS facility_id,
            f.name AS facility_name,
            cc.periodid,
            pp.name AS period_name,
            d.district_name,
            d.district_id
           FROM vw_vaccine_cumulative_coverage_by_dose cc
             JOIN vw_vaccine_facility_penta_dropouts pd ON cc.doseid = 1 AND pd.productid = cc.productid AND pd.facilityid = cc.facilityid AND pd.year = cc.year AND pd.month = cc.month
             JOIN facilities f ON f.id = pd.facilityid
             JOIN vw_districts d ON d.district_id = f.geographiczoneid
             JOIN processing_periods pp ON pp.id = pd.periodid
        )
 SELECT q.classificationclass,
    q.classification,
    q.month,
    q.year,
    q.facility_id,
    q.facility_name,
    q.periodid,
    q.period_name,
    q.district_name,
    q.district_id
   FROM q
WITH NO DATA;





      CREATE MATERIALIZED VIEW VW_PRODUCT_COVERAGE  AS
        SELECT F.GEOGRAPHICZONEID,
        D.DENOMINATORESTIMATECATEGORYID,
        D.PRODUCTID,
        D.DOSEID,
        D.MONTH PERIOD,
        P.PRIMARYNAME PRODUCT,
        d.year,
        PERIODID  ,
        MONTHLYREGULAR,
        cumulative
         FROM VW_VACCINE_CUMULATIVE_COVERAGE_BY_DOSE D
         JOIN FACILITIES F ON F.ID = D.FACILITYID
         JOIN PRODUCTS P ON PRODUCTID = P.ID
         ORDER BY PRODUCTID
         WITH NO DATA;

CREATE OR REPLACE VIEW x_vw_vaccine_cumulative_coverage_by_dose AS
  SELECT
    a.facilityid,
    a.facilityname,
    a.year,
    a.month,
    a.monthlyregular,
    a.productid,
    a.estimate,
    a.doseid,
    a.periodid,
    a.denominatorestimatecategoryid,
    a.coveragepercentage,
    a.cumulative,
    CASE
    WHEN ((a.estimate IS NOT NULL) AND (a.estimate <> 0))
      THEN (a.month * (a.estimate) :: DOUBLE PRECISION)
    ELSE NULL :: DOUBLE PRECISION
    END AS cumulativeestimate,
    CASE
    WHEN ((a.estimate IS NOT NULL) AND (a.estimate <> 0))
      THEN (((a.cumulative * (100) :: NUMERIC)) :: DOUBLE PRECISION / (a.month * (a.estimate) :: DOUBLE PRECISION))
    ELSE NULL :: DOUBLE PRECISION
    END AS cumulativepercentage
  FROM (SELECT
          av.facilityid,
          si.name                                                               AS facilityname,
          av.year,
          av.month,
          av.monthlyregular,
          av.productid,
          si.estimate,
          av.doseid,
          av.periodid,
          vd.denominatorestimatecategoryid,
          CASE
          WHEN ((si.estimate IS NOT NULL) AND (si.estimate <> 0))
            THEN ((av.monthlyregular * 100) / si.estimate)
          ELSE NULL :: BIGINT
          END                                                                   AS coveragepercentage,
          (SELECT sum(a_1.monthlyregular) AS sum
           FROM vw_vaccine_monthly_vaccinations_by_dosage a_1
           WHERE ((a_1.facilityid = av.facilityid) AND (a_1.year = av.year) AND (a_1.month <= av.month) AND
                  (a_1.productid = av.productid) AND (a_1.doseid = av.doseid))) AS cumulative
        FROM ((vw_vaccine_monthly_vaccinations_by_dosage av
          JOIN vaccine_product_doses vd ON (((vd.productid = av.productid) AND (vd.doseid = av.doseid))))
          JOIN vw_monthly_estimate si
            ON (((si.facilityid = av.facilityid) AND (si.demographicestimateid = vd.denominatorestimatecategoryid))))
        WHERE (((si.year) :: DOUBLE PRECISION = av.year) AND
               (si.demographicestimateid = vd.denominatorestimatecategoryid))) a;






         --ALTER TABLE vaccine_report_coverage_line_items ENABLE TRIGGER ALL;
--COMMIT;

/*
select * from vw_vaccine_wastage_rate_by_district limit 1;
select * from vw_vaccine_cumulative_coverage_by_district limit 1;
select * from vw_vaccine_cumulative_coverage limit 1;
select * from vw_vaccine_cumulative_consumption limit 1;
select * from vw_vaccine_cumulative_consumption_by_district limit 1;
select * from vw_vaccine_monthly_consumption limit 1;
select * from vw_vaccine_monthly_vaccinations limit 1;
select * from vw_district_bcg_mr_dropout limit 1;
select * from vw_district_bcg_mr_dropout_summary limit 1;
select * from vw_facility_bcg_mr_dropout limit 1;
select * from vw_monthly_district_estimate limit 1;
select * from vw_monthly_estimate limit 1;
select * from vw_penta_dropout_district_summary limit 1;
select * from vw_penta_dropout_facility_summary limit 1;
select * from vw_vaccine_coverage_by_dose_and_district limit 1;
select * from vw_vaccine_cumulative_consumption limit 1;
select * from vw_vaccine_cumulative_consumption_by_district limit 1;

select * from vw_vaccine_cumulative_coverage  limit 1;
select * from vw_vaccine_cumulative_coverage_by_district  limit 1;
select * from vw_vaccine_cumulative_coverage_by_dose  limit 1;
select * from vw_vaccine_district_penta_dropouts  limit 1;
select * from vw_vaccine_facility_penta_dropouts  limit 1;
select * from vw_vaccine_monthly_consumption limit 1;
select * from vw_vaccine_monthly_vaccinations limit 1;
select * from vw_vaccine_monthly_vaccinations_by_dosage limit 1;
select * from vw_vaccine_wastage_rate_by_district  limit 1;
select * from x_vw_vaccine_cumulative_coverage_by_dose  limit 1;
*/




ALTER TABLE public.vw_vaccine_monthly_vaccinations_by_dosage
  OWNER TO postgres;

-- Index: public.i_vw_vaccine_monthly_vaccination_by_dosage

-- DROP INDEX public.i_vw_vaccine_monthly_vaccination_by_dosage;

CREATE INDEX i_vw_vaccine_monthly_vaccination_by_dosage
  ON public.vw_vaccine_monthly_vaccinations_by_dosage
  USING btree
  (facilityid, productid, doseid, year, month);

