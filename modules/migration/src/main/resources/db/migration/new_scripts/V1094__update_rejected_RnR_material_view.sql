-- View: public.mv_rejected_requisition

 DROP MATERIALIZED VIEW if exists public.mv_rejected_requisition;

CREATE MATERIALIZED VIEW public.mv_rejected_requisition
TABLESPACE pg_default
AS
WITH requisition
     AS (SELECT Count(*)        AS rnrcount,
                r.id            AS rnrid,
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
                d.zone_id       AS zoneid,
                d.parent,
                d.district_name AS district,
                d.district_id   AS districtid,
                d.region_id     AS provinceid,
                d.region_name   AS province,
                f.name          AS facility,
                ft.id           AS facilitytypeid,
                ft.name         AS facilitytype,
                pr.name         AS program,
                pr.id           AS programid,
                pp.id           AS periodid,
                pp.name         AS period,
                pp.startdate,
                pp.enddate,
                f.code          AS facilitycode,
                f.id            AS facilityid,
                r.modifieddate  AS rejectiondate,
                rs.status       AS statuschange
         -- rc.commenttext
         FROM   requisitions r
                join processing_periods pp
                  ON r.periodid = pp.id
                join programs pr
                  ON pr.id = r.programid
                join facilities f
                  ON f.id = r.facilityid
                join facility_types ft
                  ON ft.id = f.typeid
                join vw_districts d
                  ON f.geographiczoneid = d.district_id
                join requisition_status_changes rs
                  ON r.id = rs.rnrid
         WHERE  rs.status :: text = ANY ( array['INITIATED'::text, 'AUTHORIZED'
                                        ::text
                                        ] )
         GROUP  BY 2,
                   3,
                   4,
                   5,
                   6,
                   7,
                   8,
                   9,
                   10,
                   11,
                   12,
                   13,
                   14,
                   15,
                   16,
                   17,
                   18,
                   19,
                   20,
                   21,
                   22,
                   23,
                   24,
                   25,
                   26,
                   27,
                   28,
                   29,
                   30,
                   31
         HAVING Count(*) > 1)
SELECT r.*,
       rc.commenttext
FROM   requisition r
       left join (SELECT comments.rnrid,
                          Array_to_string(Array_agg(comments.commenttext), ','
                          :: text)
                                               AS
                                                          commenttext
                   FROM   comments
                   GROUP  BY comments.rnrid) rc
               ON r.rnrid = rc.rnrid
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