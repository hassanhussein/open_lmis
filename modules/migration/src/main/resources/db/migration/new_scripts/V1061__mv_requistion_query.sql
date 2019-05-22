-- View: public.mv_requisition

DROP MATERIALIZED VIEW if EXISTS public.mv_requisition;

CREATE MATERIALIZED VIEW public.mv_requisition
TABLESPACE pg_default
AS
 SELECT r.id AS rnrid,
    r.status,
    d.district_name AS district,
    d.district_id AS districtid,
    d.region_id AS provinceid,
    d.region_name AS province,
    f.name AS facility,
    pr.name AS program,
    pr.id AS programid,
    pp.id AS periodid,
    pp.name AS period,
    f.code AS facilitycode,
    p.id AS productid,
    p.tracer,
    li.productcode,
    li.product,
    li.quantityrequested AS "order",
    li.quantityapproved AS approved
   FROM requisitions r
     JOIN requisition_line_items li ON r.id = li.rnrid
     JOIN processing_periods pp ON r.periodid = pp.id
     JOIN programs pr ON pr.id = r.programid
     JOIN products p ON li.productcode::text = p.code::text
     JOIN facilities f ON f.id = r.facilityid
     JOIN vw_districts d ON f.geographiczoneid = d.district_id
  WHERE r.status::text = 'RELEASED'::text AND li.quantityapproved > 0
WITH no data;

ALTER TABLE public.mv_requisition
    OWNER TO postgres;