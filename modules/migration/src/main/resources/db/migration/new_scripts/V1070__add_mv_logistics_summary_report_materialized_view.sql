-- Materialized View: public.mv_logistics_summary_report

DROP MATERIALIZED VIEW IF EXISTS public.mv_logistics_summary_report;

CREATE MATERIALIZED VIEW public.mv_logistics_summary_report AS
 SELECT li.productcode AS code,
    li.product,
    facilities.code AS facilitycode,
    facilities.name AS facility,
    facility_types.name AS facilitytype,
    li.productcategory AS category,
    li.dispensingunit AS unit,
    li.beginningbalance AS openingbalance,
    li.quantityreceived AS receipts,
    li.quantitydispensed AS issues,
    li.quantityapproved AS reorderamount,
    li.totallossesandadjustments AS adjustments,
    li.stockinhand AS closingbalance,
    r.periodid,
    r.programid,
    facilities.id AS facility_id,
    gz.zone_id,
    gz.parent,
    gz.region_id,
    gz.district_id,
    r.emergency,
    li.skipped,
    facilities.typeid AS facility_type_id,
    pps.productcategoryid,
    products.id AS productid
   FROM facilities
     JOIN facility_types ON facility_types.id = facilities.typeid
     JOIN requisitions r ON r.facilityid = facilities.id
     JOIN requisition_line_items li ON li.rnrid = r.id
     JOIN products ON products.code::text = li.productcode::text
     JOIN vw_districts gz ON gz.district_id = facilities.geographiczoneid
     JOIN programs ON r.programid = programs.id
     JOIN program_products pps ON r.programid = pps.programid AND products.id = pps.productid
     JOIN requisition_group_members ON facilities.id = requisition_group_members.facilityid
     JOIN requisition_groups ON requisition_groups.id = requisition_group_members.requisitiongroupid
     JOIN requisition_group_program_schedules ON requisition_group_program_schedules.programid = programs.id AND requisition_group_program_schedules.requisitiongroupid = requisition_groups.id
     JOIN processing_schedules ON processing_schedules.id = requisition_group_program_schedules.scheduleid
     JOIN processing_periods ON processing_periods.id = r.periodid
  WHERE r.status::text = ANY (ARRAY['AUTHORIZED'::character varying::text, 'APPROVED'::character varying::text, 'RELEASED'::character varying::text])
WITH NO DATA;

ALTER TABLE public.mv_logistics_summary_report
  OWNER TO postgres;



DROP INDEX IF EXISTS mv_logistics_summary_report_facility_id;
CREATE INDEX mv_logistics_summary_report_facility_id
ON mv_logistics_summary_report
USING btree
(facility_id);

DROP INDEX IF EXISTS mv_logistics_summary_report_facility_code;
CREATE INDEX mv_logistics_summary_report_facility_code
ON mv_logistics_summary_report
USING btree
(facilitycode);


DROP INDEX IF EXISTS mv_logistics_summary_report_productid;
CREATE INDEX mv_logistics_summary_report_productid
ON mv_logistics_summary_report
USING btree
(productid);

DROP INDEX IF EXISTS mv_logistics_summary_report_product;
CREATE INDEX mv_logistics_summary_report_product
ON mv_logistics_summary_report
USING btree
(product);

DROP INDEX IF EXISTS mv_logistics_summary_report_periodid;
CREATE INDEX mv_logistics_summary_report_periodid
ON mv_logistics_summary_report
USING btree
(periodid);