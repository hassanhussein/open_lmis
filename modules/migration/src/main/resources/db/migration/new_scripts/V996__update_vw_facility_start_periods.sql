-- View: public.vw_facility_start_periods

DROP VIEW if exists public.vw_facility_start_periods;

CREATE OR REPLACE VIEW public.vw_facility_start_periods AS
 SELECT DISTINCT f.id AS facilityid,
    f.geographiczoneid,
    ps.startdate::date AS startdate,
    ps.programid
   FROM facilities f
     JOIN requisition_group_members rgm ON f.id = rgm.facilityid
     JOIN programs_supported ps ON ps.facilityid = f.id
     JOIN facility_types ft ON ft.id = f.typeid
  WHERE f.active = true;

ALTER TABLE public.vw_facility_start_periods
    OWNER TO postgres;

