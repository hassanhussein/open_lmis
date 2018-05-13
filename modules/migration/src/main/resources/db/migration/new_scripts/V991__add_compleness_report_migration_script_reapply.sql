DROP MATERIALIZED VIEW IF EXISTS vw_requisitions_submitted_status;

CREATE MATERIALIZED VIEW vw_requisitions_submitted_status
TABLESPACE pg_default
AS
 SELECT DISTINCT ranked_facilities.*
   FROM ( SELECT items.*,
            rank() OVER (PARTITION BY items.facilityid, items.periodid ORDER BY items.statussubmissioneddate DESC) AS rank
           FROM ( SELECT r.*,
                    r.createddate::date AS statussubmissioneddate
                   FROM requisitions r
                     JOIN ( SELECT rs_1.createddate,
                            rs_1.rnrid
                           FROM requisition_status_changes rs_1
                          WHERE rs_1.status::text = 'SUBMITTED'::text) rs ON rs.rnrid = r.id) items
          ORDER BY items.facilityid, items.periodid, items.statussubmissioneddate DESC) ranked_facilities
  WHERE ranked_facilities.rank = 1
WITH DATA;

ALTER TABLE public.vw_requisitions_submitted_status OWNER TO postgres;
-------------------------------------------------------------------------

DROP VIEW if exists vw_facility_start_periods;

CREATE OR REPLACE VIEW vw_facility_start_periods AS
 SELECT f.id AS facilityid,
    f.geographiczoneid,
    ps.startdate::date AS startdate
   FROM facilities f
     JOIN requisition_group_members rgm ON f.id = rgm.facilityid
     JOIN programs_supported ps ON ps.facilityid = f.id
     JOIN facility_types ft ON ft.id = f.typeid
  WHERE f.active = true;

  ALTER TABLE public.vw_facility_start_periods OWNER TO postgres;