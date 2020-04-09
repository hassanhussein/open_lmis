-- View: public.mv_rejected_requisition

DROP MATERIALIZED VIEW if exists public.mv_rejected_requisition;

CREATE MATERIALIZED VIEW public.mv_rejected_requisition
TABLESPACE pg_default
AS
SELECT DISTINCT r.id AS rnrid,
    r.status,
    r.createddate,
    r.createdby,
	r.modifieddate,
	r.modifiedby,
    r.emergency,
    r.allocatedbudget,
    r.clientsubmittedtime,
    r.clientsubmittednotes,
    r.sourceapplication,
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
	r.modifieddate rejectiondate,
	rs.status statuschange,
    rc.commenttext
   FROM requisitions r
     JOIN processing_periods pp ON r.periodid = pp.id
     JOIN programs pr ON pr.id = r.programid
     JOIN facilities f ON f.id = r.facilityid
     JOIN facility_types ft ON ft.id = f.typeid
     JOIN vw_districts d ON f.geographiczoneid = d.district_id
     JOIN (select rnrid, array_to_string( array_agg(commenttext),',' ) as commenttext 
		   from comments 		  
		   group by rnrid)  rc on r.id =rc.rnrid
     JOIN requisition_status_changes rs ON r.id = rs.rnrid
  WHERE rs.status::text = ANY ( ARRAY['INITIATED'::text, 'AUTHORIZED'])
WITH DATA;

ALTER TABLE public.mv_rejected_requisition
    OWNER TO postgres;


CREATE INDEX date_rejected_index
    ON public.mv_rejected_requisition USING btree
    (startdate, enddate)
    TABLESPACE pg_default;
CREATE INDEX progrogram_rejected_index
    ON public.mv_rejected_requisition USING btree
    (program COLLATE pg_catalog."default")
    TABLESPACE pg_default;
CREATE INDEX rnr_id_rejected_index
    ON public.mv_rejected_requisition USING btree
    (rnrid)
    TABLESPACE pg_default;
CREATE INDEX zone_rejected_index
    ON public.mv_rejected_requisition USING btree
    (districtid)
    TABLESPACE pg_default;
	
	