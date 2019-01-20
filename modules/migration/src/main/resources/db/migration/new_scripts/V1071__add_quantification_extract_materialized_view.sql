DROP MATERIALIZED VIEW if exists public.quantification_extraction;
DROP MATERIALIZED VIEW if exists public.mv_quantification_extraction;


CREATE MATERIALIZED VIEW public.mv_quantification_extraction AS
 SELECT
    productCategoryId,
    products.id productId,
    processing_schedules.name scheduleName,
    processing_periods.name periodName,
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
    products.packsize uom,
    li.dispensingunit AS unit,
    li.quantitydispensed AS issues
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
  WHERE r.emergency = false
  ORDER BY li.productcategory, li.product, facilities.name
WITH NO DATA;


-- UPDATE INDEXIS
-- Index: public.i_quantification_extraction_periods

DROP INDEX IF EXISTS public.i_quantification_extraction_periods;

CREATE INDEX i_quantification_extraction_periods
  ON public.mv_quantification_extraction
  USING btree
  (period);

-- Index: public.i_quantification_extraction_programs

 DROP INDEX IF EXISTS public.i_quantification_extraction_programs;

CREATE INDEX i_quantification_extraction_programs
  ON public.mv_quantification_extraction
  USING btree
  (program);

-- Index: public.i_quantification_extractions_code

DROP INDEX IF EXISTS public.i_quantification_extractions_code;

CREATE INDEX i_quantification_extractions_code
  ON public.mv_quantification_extraction
  USING btree
  (code COLLATE pg_catalog."default");

-- Index: public.i_quantification_extractions_facility

DROP INDEX IF EXISTS public.i_quantification_extractions_facility;

CREATE INDEX i_quantification_extractions_facility
  ON public.mv_quantification_extraction
  USING btree
  (facility COLLATE pg_catalog."default");

-- Index: public.i_quantification_extractions_facilitycode

 DROP INDEX IF EXISTS public.i_quantification_extractions_facilitycode;

CREATE INDEX i_quantification_extractions_facilitycode
  ON public.mv_quantification_extraction
  USING btree
  (facilitycode COLLATE pg_catalog."default");

-- Index: public.i_quantification_extractions_program_period_code_index

DROP INDEX IF EXISTS public.i_quantification_extractions_program_period_code_index;

CREATE INDEX i_quantification_extractions_program_period_code_index
  ON public.mv_quantification_extraction
  USING btree
  (program, period, code COLLATE pg_catalog."default", facilitycode COLLATE pg_catalog."default", facility COLLATE pg_catalog."default");





