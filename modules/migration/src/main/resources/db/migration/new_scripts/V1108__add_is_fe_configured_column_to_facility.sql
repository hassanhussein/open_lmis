
DROP MATERIALIZED VIEW if exists public.mv_requisition;
ALTER TABLE public.facilities
    drop COLUMN if exists feconfigured ;
ALTER TABLE public.facilities
    ADD COLUMN feconfigured boolean;

update facilities set feconfigured =true where id in
(select distinct facilityid from requisitions r
where lower(r.sourceapplication) in ('elmis_fe' ,'elmis_dm'));


-- View: public.mv_requisition



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
	f.feconfigured as feconfigured,
    ft.id AS facilitytypeid,
    ft.name AS facilitytype,
    ft.nominalmaxmonth,
    ft.nominaleop,
	ft.code as facilitytypecode,
    pf.code AS pfcode,
    pr.name AS program,
    pr.id AS programid,
    pp.id AS periodid,
    pp.name AS period,
    pp.startdate,
    pp.enddate,
    prp.productcategoryid AS categoryid,
    p.productgroupid,
    pp.scheduleid,
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
    li.amc,
    li.calculatedorderquantity,
    li.newpatientcount,
    li.stockoutdays,
    li.maxmonthsofstock,
    li.maxstockquantity,
    li.packstoship,
    li.fullsupply,
    round(100::numeric * ((li.normalizedconsumption - COALESCE(li.amc, 0)) / COALESCE(NULLIF(li.amc, 0), 1))::numeric, 4) AS consumptionrate
   FROM requisitions r
     JOIN requisition_line_items li ON r.id = li.rnrid
     JOIN processing_periods pp ON r.periodid = pp.id
     JOIN programs pr ON pr.id = r.programid
     JOIN products p ON li.productcode::text = p.code::text
     JOIN program_products prp ON p.id = prp.productid AND prp.programid = pr.id
     JOIN facilities f ON f.id = r.facilityid
     JOIN facility_types ft ON ft.id = f.typeid
     JOIN vw_districts d ON f.geographiczoneid = d.district_id
     JOIN product_forms pf ON p.formid = pf.id
     JOIN dosage_units ds ON ds.id = p.dosageunitid
     WHERE (r.status::text = ANY (ARRAY['IN_APPROVAL'::text, 'RELEASED'::text, 'APPROVED'::text, 'RELEASED_NO_ORDER'::text])) AND li.skipped = false
WITH no DATA;

ALTER TABLE public.mv_requisition
    OWNER TO postgres;


CREATE INDEX date_index
    ON public.mv_requisition USING btree
    (startdate, enddate)
    TABLESPACE pg_default;
CREATE INDEX idx_consumption
    ON public.mv_requisition USING btree
    (consumption)
    TABLESPACE pg_default;
CREATE INDEX idx_dispense
    ON public.mv_requisition USING btree
    (dispensed)
    TABLESPACE pg_default;
CREATE INDEX idx_mv_req_end_date
    ON public.mv_requisition USING btree
    (enddate)
    TABLESPACE pg_default;
CREATE INDEX idx_mv_req_start_date
    ON public.mv_requisition USING btree
    (startdate)
    TABLESPACE pg_default;
CREATE INDEX idx_mv_requ_period
    ON public.mv_requisition USING btree
    (periodid)
    TABLESPACE pg_default;
CREATE INDEX idx_mv_requisition_status
    ON public.mv_requisition USING btree
    (status COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX progrogram_index
    ON public.mv_requisition USING btree
    (program COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX rnr_id_index
    ON public.mv_requisition USING btree
    (rnrid)
    TABLESPACE pg_default;
CREATE INDEX zone_index
    ON public.mv_requisition USING btree
    (districtid)
    TABLESPACE pg_default;