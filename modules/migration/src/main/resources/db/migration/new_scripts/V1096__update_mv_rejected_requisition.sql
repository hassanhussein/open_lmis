-- View: public.mv_rejected_requisition

DROP MATERIALIZED VIEW if exists public.mv_rejected_requisition;
-- Table: public.requisition_rejection
 DROP TABLE if exists public.requisition_rejection;
DROP SEQUENCE if exists public.requisition_rejection_id_seq;

CREATE SEQUENCE public.requisition_rejection_id_seq;
CREATE TABLE public.requisition_rejection
(
    id integer NOT NULL DEFAULT nextval('requisition_rejection_id_seq'::regclass),
    rnr_id integer NOT NULL,
    rnr_status_from character varying COLLATE pg_catalog."default",
    rnr_status_to character varying COLLATE pg_catalog."default",
    description character varying COLLATE pg_catalog."default",
    createdby integer,
    createddate timestamp without time zone DEFAULT now(),
    modifiedby integer,
    modifieddate timestamp without time zone DEFAULT now(),
    CONSTRAINT requisition_rejection_pkey PRIMARY KEY (id),
    CONSTRAINT requisition_rejection_id_fkey FOREIGN KEY (rnr_id)
        REFERENCES public.requisitions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.requisition_rejection
    OWNER to postgres;
    ----------------------------------------------------------------------------------------------------------
CREATE MATERIALIZED VIEW public.mv_rejected_requisition
TABLESPACE pg_default
AS
 WITH requisition AS (
         SELECT count(*) AS rnrcount,
            r_1.id AS rnrid,
            r_1.status,
            r_1.createddate,
            r_1.createdby,
            r_1.modifieddate,
            r_1.modifiedby,
            r_1.emergency,
            r_1.allocatedbudget,
            r_1.clientsubmittedtime,
            r_1.clientsubmittednotes,
            r_1.sourceapplication,
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
            r_1.modifieddate AS rejectiondate,
            rs.status AS statuschange
           FROM requisitions r_1
             JOIN processing_periods pp ON r_1.periodid = pp.id
             JOIN programs pr ON pr.id = r_1.programid
             JOIN facilities f ON f.id = r_1.facilityid
             JOIN facility_types ft ON ft.id = f.typeid
             JOIN vw_districts d ON f.geographiczoneid = d.district_id
             JOIN requisition_status_changes rs ON r_1.id = rs.rnrid
          WHERE rs.status::text = ANY (ARRAY['INITIATED'::text, 'AUTHORIZED'::text])
          GROUP BY r_1.id, r_1.status, r_1.createddate, r_1.createdby, r_1.modifieddate, r_1.modifiedby, r_1.emergency, r_1.allocatedbudget, r_1.clientsubmittedtime, r_1.clientsubmittednotes, r_1.sourceapplication, d.zone_id, d.parent, d.district_name, d.district_id, d.region_id, d.region_name, f.name, ft.id, ft.name, pr.name, pr.id, pp.id, pp.name, pp.startdate, pp.enddate, f.code, f.id, r_1.modifieddate, rs.status
         HAVING count(*) > 1
        )
 SELECT r.rnrcount,
    r.rnrid,
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
    r.zoneid,
    r.parent,
    r.district,
    r.districtid,
    r.provinceid,
    r.province,
    r.facility,
    r.facilitytypeid,
    r.facilitytype,
    r.program,
    r.programid,
    r.periodid,
    r.period,
    r.startdate,
    r.enddate,
    r.facilitycode,
    r.facilityid,
    r.rejectiondate,
    r.statuschange,
    rc.commenttext as comments,
	rj.description as commenttext
   FROM requisition r
     LEFT JOIN ( SELECT comments.rnrid,
            array_to_string(array_agg(comments.commenttext), ','::text) AS commenttext
           FROM comments
          GROUP BY comments.rnrid) rc ON r.rnrid = rc.rnrid
		left join requisition_rejection rj on rj.rnr_id =r.rnrid
WITH no DATA;

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