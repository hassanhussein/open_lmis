

 DROP MATERIALIZED VIEW IF EXISTS FACILITY__CLASSIFICATION_categorization_view;

CREATE MATERIALIZED VIEW public.FACILITY__CLASSIFICATION_categorization_view AS
WITH q as (
SELECT
  CASE WHEN pd.dropout <= 10 AND cc.coveragepercentage >= 90
    THEN 'good'
  WHEN pd.dropout > 10 AND cc.coveragepercentage >= 90
    THEN 'normal'
  WHEN pd.dropout <= 10 AND cc.coveragepercentage <= 90
    THEN 'warn'
  ELSE 'bad'
  END AS classificationclass,
  CASE WHEN pd.dropout <= 10 AND cc.coveragepercentage >= 90
    THEN 'Cat_1'
  WHEN pd.dropout > 10 AND cc.coveragepercentage >= 90
    THEN 'Cat_2'
  WHEN pd.dropout <= 10 AND cc.coveragepercentage <= 90
    THEN 'Cat_3'
  ELSE 'Cat_4'
  END AS classification,

  cc.month,
  cc.year,
  pd.facilityid as facility_id,
  f.name as facility_name,
  cc.periodid,
  pp.name as period_name,
  d.district_name,
  d.district_id
FROM vw_vaccine_cumulative_coverage_by_dose cc
  JOIN vw_vaccine_facility_penta_dropouts pd
    ON cc.doseid = 1
       AND pd.productid = cc.productid
       AND pd.facilityid = cc.facilityid
       AND pd.year = cc.year
       AND pd.month = cc.month
  join facilities f on f.id = pd.facilityid
      JOIN vw_districts d on d.district_id = f.geographiczoneid
      JOIN processing_periods pp on pp.id = pd.periodid
)
SELECT * FROM q
WITH DATA;





-- Classification report

 DROP MATERIALIZED VIEW IF EXISTS FACILITY_CLASSIFICATION_view;

CREATE MATERIALIZED VIEW FACILITY_CLASSIFICATION_view AS

SELECT
  b.*,
  CASE WHEN b.wastageClassification = 'good' AND b.coverageClassification = 'good'
    THEN 'Class A'
  WHEN b.wastageClassification != 'good' AND b.coverageClassification = 'good'
    THEN 'Class B'
  WHEN b.wastageClassification = 'good' AND b.coverageClassification != 'good'
    THEN 'Class C'
  ELSE 'Class D'
  END AS classification,
  CASE WHEN b.wastageClassification = 'good' AND b.coverageClassification = 'good'
    THEN 'good'
  WHEN b.wastageClassification != 'good' AND b.coverageClassification = 'good'
    THEN 'normal'
  WHEN b.wastageClassification = 'good' AND b.coverageClassification != 'good'
    THEN 'warn'
  ELSE 'bad'
  END AS classificationClass,
  f.name as facilityname,
  d.district_name as district,
  pp.name as period_name,
  f.id as facility_id,
  d.district_id
FROM (
       SELECT
         a.*,
         (
           CASE
           WHEN a.percentageWasted < a.targetWastageGood
             THEN 'good'
           WHEN a.percentageWasted < a.targetcoveragewarn
             THEN 'normal'
           WHEN a.percentageWasted < a.targetcoveragebad
             THEN 'warn'
           ELSE 'bad' END) AS wastageClassification

       FROM (SELECT
               consumption.facilityid,
               consumption.month,
               consumption.year,
               consumption.productid product_id,
               consumption.periodid period_id,
               consumption.cumulative                           cumulativeConsumption,
               consumption.monthlyconsumption,
               vaccinated.monthlyregular,
               vaccinated.doseid,
               vaccinated.cumulative                            cumulativeCoverage,
               vaccinated.cumulativepercentage,
               f.name            AS                             facility_name,
               D.district_name,

               (
                 CASE
                 WHEN vaccinated.coveragepercentage > target.targetcoverageGood
                   THEN 'good'
                 WHEN vaccinated.coveragepercentage > target.targetcoveragewarn
                   THEN 'normal'
                 WHEN vaccinated.coveragepercentage > target.targetcoveragebad
                   THEN 'warn'
                 ELSE 'bad' END) AS                             coverageClassification,

               target.*,
               (consumption.cumulative - vaccinated.cumulative) wastage,
               pp.name           AS                             period,
               CASE WHEN consumption.cumulative IS NOT NULL AND consumption.cumulative != 0
                 THEN (100 * (consumption.cumulative - vaccinated.cumulative)) / consumption.cumulative
               ELSE NULL END     AS                             percentageWasted
             FROM vw_vaccine_cumulative_consumption consumption
               JOIN vw_vaccine_cumulative_coverage_by_dose vaccinated
                 ON consumption.productid = vaccinated.productid
                    AND consumption.facilityId = vaccinated.facilityId
                    AND consumption.month = vaccinated.month
                    AND consumption.year = vaccinated.year
               JOIN processing_periods pp ON pp.id = vaccinated.periodid
               JOIN facilities f ON f.id = vaccinated.facilityid
               JOIN vw_districts D ON D.district_id = f.geographiczoneid
               JOIN vaccine_product_targets target
                 ON target.productid = consumption.productid
                WHERE vaccinated.doseid = (select max(vd.doseid) from vaccine_product_doses vd WHERE vd.productid = vaccinated.productid)
             ORDER BY D.district_name, f.name
            ) a
     ) b
  JOIN facilities f ON f.id = b.facilityid
  JOIN processing_periods pp ON pp.id = b.period_id
  JOIN vw_districts d ON d.district_id = f.geographiczoneid
WITH DATA;

