DROP MATERIALIZED VIEW IF EXISTS public.mv_quantification_extraction;

CREATE MATERIALIZED VIEW public.mv_quantification_extraction AS
 SELECT pps.productcategoryid,
    products.id AS productid,
    processing_schedules.name AS schedulename,
    processing_periods.name AS periodname,
    programs.id AS program,
    requisition_group_program_schedules.scheduleid AS schedule,
    processing_periods.id AS period,
    gz.district_id,
    gz.region_id,
    gz.zone_id,
    gz.parent,
    processing_periods.startdate,
    processing_periods.enddate,
    li.productcode AS code,
    li.product,
    facilities.code AS facilitycode,
    facilities.name AS facility,
    facility_types.name AS facilitytype,
    li.productcategory AS category,
    products.packsize AS uom,
    li.dispensingunit AS unit,
    li.quantitydispensed AS issues,
    li.normalizedConsumption,
    facilities.id facilityId
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
  WHERE r.emergency = false and processing_periods.enableOrder = true
  ORDER BY li.productcategory, li.product, facilities.name
WITH NO DATA;
