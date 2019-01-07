--DROP MATERIALIZED VIEW public.mv_stock_imbalance_by_facility_report;
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
    pgp.productcategoryid,
    p.id AS productid,
        CASE
            WHEN li.stockinhand = 0 THEN 'SO'::text
            ELSE
            CASE
                WHEN li.amc > 0 AND li.stockinhand > 0 THEN
                CASE
                    WHEN round(li.stockinhand::numeric / li.amc::numeric, 2) < fap.minmonthsofstock THEN 'US'::text
                    WHEN round(li.stockinhand::numeric / li.amc::numeric, 2) >= fap.minmonthsofstock AND round(li.stockinhand::numeric / li.amc::numeric, 2) <= fap.maxmonthsofstock::numeric THEN 'SP'::text
                    WHEN round(li.stockinhand::numeric / li.amc::numeric, 2) > fap.maxmonthsofstock::numeric THEN 'OS'::text
                    ELSE NULL::text
                END
                ELSE 'UK'::text
            END
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
    li.quantityapproved AS ordered
   FROM processing_periods pp
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

ALTER TABLE public.mv_stock_imbalance_by_facility_report
  OWNER TO postgres;



DROP INDEX IF EXISTS mv_stock_imbalance_by_facility_report_facility_id;
CREATE INDEX mv_stock_imbalance_by_facility_report_facility_id
ON mv_stock_imbalance_by_facility_report
USING btree
(facility_id);

DROP INDEX IF EXISTS mv_stock_imbalance_by_facility_report_facility_code;
CREATE INDEX mv_stock_imbalance_by_facility_report_facility_code
ON mv_stock_imbalance_by_facility_report
USING btree
(facilitycode);


DROP INDEX IF EXISTS mv_requisition_adjustment_index_productid;
CREATE INDEX mv_stock_imbalance_by_facility_report_facility_code_productid
ON mv_stock_imbalance_by_facility_report
USING btree
(productid);

DROP INDEX IF EXISTS mv_requisition_adjustment_index_product;
CREATE INDEX mv_requisition_adjustment_index_product
ON mv_stock_imbalance_by_facility_report
USING btree
(product);

DROP INDEX IF EXISTS mv_stock_imbalance_by_facility_report_periodid;
CREATE INDEX mv_stock_imbalance_by_facility_report_periodid
ON mv_stock_imbalance_by_facility_report
USING btree
(periodid);