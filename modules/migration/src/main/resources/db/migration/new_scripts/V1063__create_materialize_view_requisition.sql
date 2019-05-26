-- View: public.mv_requisition

DROP MATERIALIZED VIEW IF EXISTS public.mv_requisition;

CREATE MATERIALIZED VIEW public.mv_requisition
TABLESPACE pg_default
AS
 SELECT r.id AS rnrid,
    r.status,
    r.emergency,
    d.zone_id AS zoneid,
    d.parent,
    d.district_name AS district,
    d.district_id AS districtid,
    d.region_id AS provinceid,
    d.region_name AS province,
    f.name AS facility,
    ft.id AS facilitytypeid,
    ft.name AS facilitytype,
    pr.name AS program,
    pr.id AS programid,
    pp.id AS periodid,
    pp.name AS period,
    pp.startdate,
    pp.enddate,
    f.code AS facilitycode,
    f.id AS facilityid,
    p.id AS productid,
    p.tracer,
    (p.code::text || '_'::text) || f.code::text AS facprodcode,
    ((((((p.primaryname::text || ' '::text) || COALESCE(p.strength, ''::character varying)::text) || ' '::text) || COALESCE(ds.code, ''::character varying)::text) || ' ('::text) || COALESCE(p.dispensingunit, '-'::character varying)::text) || ')'::text AS productdispalayname,
    li.productcode,
    li.product,
    li.quantityrequested AS "order",
    li.quantityapproved AS approved,
    li.quantitydispensed AS dispensed,
    li.quantityrequested AS requested,
    li.quantityreceived AS recieved,
    li.normalizedconsumption AS consumption,
    li.packsize,
    li.productcategory,
    li.dispensingunit,
    li.beginningbalance AS balance,
    li.stockinhand,
    li.totallossesandadjustments AS adjustment,
    li.amc
   FROM requisitions r
     JOIN requisition_line_items li ON r.id = li.rnrid
     JOIN processing_periods pp ON r.periodid = pp.id
     JOIN programs pr ON pr.id = r.programid
     JOIN products p ON li.productcode::text = p.code::text
     JOIN facilities f ON f.id = r.facilityid
     JOIN facility_types ft ON ft.id = f.typeid
     JOIN vw_districts d ON f.geographiczoneid = d.district_id
     JOIN dosage_units ds ON ds.id = p.dosageunitid
  WHERE r.status::text = 'RELEASED'::text AND li.quantityapproved > 0
WITH NO DATA;

ALTER TABLE public.mv_requisition
    OWNER TO postgres;