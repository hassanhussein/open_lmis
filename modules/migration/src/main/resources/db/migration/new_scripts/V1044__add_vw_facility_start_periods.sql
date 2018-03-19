DROP VIEW IF EXISTS vw_facility_start_periods;

CREATE OR REPLACE VIEW vw_facility_start_periods AS
SELECT f.id AS facilityid,
    f.geographiczoneid,
    ps.startdate::date AS startdate
   FROM facilities f
     JOIN requisition_group_members rgm ON f.id = rgm.facilityid
     JOIN programs_supported ps ON ps.facilityid = f.id
     JOIN facility_types ft ON ft.id = f.typeid
  WHERE f.active = true AND ft.levelid = (( SELECT max(facility_types.levelid) AS max FROM facility_types))
  and programid = (select id from programs where enableivdform = 't' limit 1);
ALTER TABLE vw_facility_start_periods OWNER TO postgres;


