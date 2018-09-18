-- View: public.vw_user_districts
 DROP VIEW if exists public.vw_user_districts;
 -- View: public.vw_user_facilities

 DROP VIEW  if exists public.vw_user_facilities;

 CREATE OR REPLACE VIEW public.vw_user_facilities AS
  SELECT DISTINCT f.id AS facility_id,
     f.geographiczoneid AS district_id,
     rg.id AS requisition_group_id,
     ra.userid AS user_id,
     ra.programid AS program_id
    FROM facilities f
      JOIN requisition_group_members m ON m.facilityid = f.id
      JOIN requisition_groups rg ON rg.id = m.requisitiongroupid
      JOIN supervisory_nodes sn ON sn.id = rg.supervisorynodeid
      JOIN role_assignments ra ON ra.supervisorynodeid = sn.id OR ra.supervisorynodeid = sn.parentid
      JOIN programs_supported ps on ps.facilityid=f.id and ps.programid=ra.programid
 	 where f.active=true;
 ALTER TABLE public.vw_user_facilities
     OWNER TO postgres;






CREATE OR REPLACE VIEW public.vw_user_districts AS
 SELECT DISTINCT vw_user_facilities.user_id,
    vw_user_facilities.district_id,
    vw_user_facilities.program_id
   FROM vw_user_facilities;

ALTER TABLE public.vw_user_districts
    OWNER TO postgres;