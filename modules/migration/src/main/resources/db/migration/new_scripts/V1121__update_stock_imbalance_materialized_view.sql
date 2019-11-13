DROP MATERIALIZED VIEW if exists public.mv_stock_imbalance_by_facility_report;
CREATE MATERIALIZED VIEW public.mv_stock_imbalance_by_facility_report AS
 SELECT gz.region_name AS supplyingfacility,
    gz.region_name,
    gz.district_name,
    gz.zone_name,
    f.code AS facilitycode,
    li.productcode,
    f.name AS facility,
    li.product,
    ft.name AS facilitytypename,
    gz.district_name AS location,
    pp.name AS processing_period_name,
    li.stockinhand,
    li.stockoutdays,
    to_char(pp.startdate, 'Mon'::text) AS asmonth,
    date_part('year'::text, pp.startdate) AS year,
    r.periodid,
    r.programid,
    f.id AS facility_id,
    gz.zone_id,
    gz.parent,
    gz.region_id,
    gz.district_id,
    r.emergency,
    li.skipped,
    pg.code AS program,
    f.typeid AS facility_type_id,
    ft.isprimaryhealthcare,
    pgp.productcategoryid,
    p.id AS productid,
    p.tracer,
    p.fullSupply,
        CASE
            WHEN COALESCE(li.amc, 0) = 0 THEN 'UK'::text
            WHEN COALESCE(li.stockinhand, 0) = 0 THEN 'SO'::text
            WHEN round((li.stockinhand / li.amc)::numeric, 1) < fap.minmonthsofstock THEN 'US'::text
            WHEN round((li.stockinhand / li.amc)::numeric, 1) <= fap.maxmonthsofstock::numeric THEN 'SP'::text
            WHEN round((li.stockinhand / li.amc)::numeric, 1) > fap.minmonthsofstock THEN 'OS'::text
            ELSE 'UK'::text
        END AS status,
        CASE
            WHEN COALESCE(li.amc, 0) = 0 THEN 0::numeric
            ELSE trunc(round(li.stockinhand::numeric / li.amc::numeric, 2), 1)
        END AS mos,
    li.amc,
    COALESCE(
        CASE
            WHEN (COALESCE(li.amc, 0) * ft.nominalmaxmonth - li.stockinhand) < 0 THEN 0
            ELSE COALESCE(li.amc, 0) * ft.nominalmaxmonth - li.stockinhand
        END, 0) AS required,
    li.quantityapproved AS ordered,
    ps.name AS schedule,
    ps.id AS scheduleid,
    f.longitude,
    f.latitude,
    f.mainphone,
    pgp.currentprice
   FROM processing_periods pp
     JOIN processing_schedules ps ON pp.scheduleid = ps.id
     JOIN requisitions r ON pp.id = r.periodid
     JOIN requisition_line_items li ON li.rnrid = r.id
     JOIN facilities f ON f.id = r.facilityid
     JOIN facility_types ft ON ft.id = f.typeid
     JOIN products p ON p.code::text = li.productcode::text
     JOIN vw_districts gz ON gz.district_id = f.geographiczoneid
     JOIN programs pg ON pg.id = r.programid
     JOIN program_products pgp ON r.programid = pgp.programid AND p.id = pgp.productid
     JOIN facility_approved_products fap ON ft.id = fap.facilitytypeid AND fap.programproductid = pgp.id
  WHERE li.skipped = false AND (li.beginningbalance > 0 OR li.quantityreceived > 0 OR li.quantitydispensed > 0 OR abs(li.totallossesandadjustments) > 0 OR li.amc > 0) AND (li.beginningbalance > 0 OR li.quantityreceived > 0 OR li.quantitydispensed > 0 OR abs(li.totallossesandadjustments) > 0 OR li.amc > 0) AND (r.status::text = ANY (ARRAY['IN_APPROVAL'::character varying::text, 'RELEASED'::character varying::text, 'APPROVED'::character varying::text, 'RELEASED_NO_ORDER'::character varying::text]))
WITH NO DATA;