-- View: public.vw_requisitions_submitted_status

 DROP MATERIALIZED VIEW if exists public.vw_requisitions_submitted_status;

CREATE MATERIALIZED VIEW public.vw_requisitions_submitted_status
TABLESPACE pg_default
AS
 SELECT DISTINCT ranked_facilities.id,
    ranked_facilities.facilityid,
    ranked_facilities.programid,
    ranked_facilities.periodid,
    ranked_facilities.status,
    ranked_facilities.fullsupplyitemssubmittedcost,
    ranked_facilities.nonfullsupplyitemssubmittedcost,
    ranked_facilities.supervisorynodeid,
    ranked_facilities.createdby,
    ranked_facilities.createddate,
    ranked_facilities.modifiedby,
    ranked_facilities.modifieddate,
    ranked_facilities.emergency,
    ranked_facilities.allocatedbudget,
    ranked_facilities.clientsubmittedtime,
    ranked_facilities.clientsubmittednotes,
    ranked_facilities.sourceapplication,
    ranked_facilities.statussubmissioneddate,
    ranked_facilities.rank
   FROM ( SELECT items.id,
            items.facilityid,
            items.programid,
            items.periodid,
            items.status,
            items.fullsupplyitemssubmittedcost,
            items.nonfullsupplyitemssubmittedcost,
            items.supervisorynodeid,
            items.createdby,
            items.createddate,
            items.modifiedby,
            items.modifieddate,
            items.emergency,
            items.allocatedbudget,
            items.clientsubmittedtime,
            items.clientsubmittednotes,
            items.sourceapplication,
            items.statussubmissioneddate,
            rank() OVER (PARTITION BY items.facilityid,items.programid, items.periodid ORDER BY items.statussubmissioneddate DESC) AS rank
           FROM ( SELECT r.id,
                    r.facilityid,
                    r.programid,
                    r.periodid,
                    r.status,
                    r.fullsupplyitemssubmittedcost,
                    r.nonfullsupplyitemssubmittedcost,
                    r.supervisorynodeid,
                    r.createdby,
                    r.createddate,
                    r.modifiedby,
                    r.modifieddate,
                    r.emergency,
                    r.allocatedbudget,
                    r.clientsubmittedtime,
                    r.clientsubmittednotes,
                    r.sourceapplication,
                    r.createddate::date AS statussubmissioneddate
                   FROM requisitions r
                     JOIN ( SELECT rs_1.createddate,
                            rs_1.rnrid
                           FROM requisition_status_changes rs_1
                          WHERE rs_1.status::text = 'SUBMITTED'::text) rs ON rs.rnrid = r.id) items
          ORDER BY items.facilityid, items.periodid, items.statussubmissioneddate DESC) ranked_facilities
  WHERE ranked_facilities.rank = 1
WITH DATA;

ALTER TABLE public.vw_requisitions_submitted_status
    OWNER TO postgres;