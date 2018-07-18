-- Materialized View: public.vw_vaccine_facility_classification

 DROP MATERIALIZED VIEW IF EXISTS public.vw_vaccine_facility_classification;

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
WITH DATA;

ALTER TABLE public.vw_vaccine_facility_classification
  OWNER TO postgres;


  -- Materialized View: public.vw_vaccine_classification_by_districts

 DROP MATERIALIZED VIEW IF EXISTS public.vw_vaccine_classification_by_districts;

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
WITH DATA;

ALTER TABLE public.vw_vaccine_classification_by_districts
  OWNER TO postgres;

