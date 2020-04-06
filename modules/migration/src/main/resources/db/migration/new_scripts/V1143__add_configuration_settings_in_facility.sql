DO $$
    BEGIN
        BEGIN
            ALTER TABLE facilities ADD COLUMN isHIDTU Boolean DEFAULT FALSE;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column isHIDTU already exists in Facilities.';
        END;
    END;
$$;


DO $$
    BEGIN
        BEGIN
            ALTER TABLE facilities ADD COLUMN numberOfStaff INTEGER;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column numberOfStaff already exists in Facilities.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE requisitions ADD COLUMN numberOfCumulativeCases INTEGER;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column numberOfCumulativeCases already exists in requisitions.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE requisitions ADD COLUMN patientOnTreatment INTEGER;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column patientOnTreatment already exists in requisitions.';
        END;
    END;
$$;


DROP TABLE IF EXISTS patient_records;
CREATE TABLE public.patient_records
(
  id serial,
  rnrId integer NOT NULL,
  newConfirmedCase integer,
  totalRecovered integer,
  totalDeath integer,
  transfer integer,
  numberOfCumulativeCases integer,
  patientOnTreatment integer,
  CONSTRAINT patient_records_pkey PRIMARY KEY (id),
  CONSTRAINT patient_records_rnrid_fkey FOREIGN KEY (rnrid)
      REFERENCES public.requisitions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


DO $$
    BEGIN
        BEGIN
            ALTER TABLE programs ADD COLUMN canTrackCovid Boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column canTrackCovid already exists in programs.';
        END;
    END;
$$;



DROP FUNCTION IF EXISTS public.fn_delete_rnr(integer);

CREATE OR REPLACE FUNCTION public.fn_delete_rnr(in_rnrid integer)
  RETURNS character varying AS
$BODY$
DECLARE i RECORD;
DECLARE j RECORD;
DECLARE li integer;
DECLARE v_rnr_id integer;
DECLARE v_rli_id integer;
DECLARE msg character varying(2000);
BEGIN
li := 0;
msg := 'Requisition id ' || in_rnrid || ' not found. No record deleted.';
select id into v_rnr_id from requisitions where id = in_rnrid;
if v_rnr_id > 0 then
msg = 'Requisition id ' || in_rnrid || ' deleted successfully.';
DELETE  FROM  requisition_line_item_losses_adjustments where requisitionlineitemid
in (select id from requisition_line_items where rnrid in (select id from requisitions where id = v_rnr_id));
select id into li from requisition_line_items where rnrid = in_rnrid limit 1;
if li > 0 then
DELETE FROM requisition_line_items WHERE rnrid= in_rnrid;
end if;
DELETE FROM patient_records WHERE rnrid = v_rnr_id;
DELETE FROM requisition_source_of_funds WHERE rnrid = v_rnr_id;
DELETE FROM requisition_status_changes where rnrid = v_rnr_id;
DELETE FROM regimen_line_items where rnrid = v_rnr_id;
DELETE FROM pod_line_items where podid in (select id from pod where orderid = v_rnr_id);
DELETE FROM pod where orderid = v_rnr_id;
DELETE FROM orders where id = v_rnr_id;
DELETE FROM comments where rnrid = v_rnr_id;
DELETE FROM equipment_bio_chemistry_tests where equipmentlineitemid in (select id from equipment_status_line_items  where rnrid = v_rnr_id);
DELETE FROM equipment_test_type_operational_status WHERE equipmentlineitemid in (select id from equipment_status_line_items  where rnrid = v_rnr_id);
DELETE FROM equipment_test_item_tests WHERE equipmentlineitemid in  (select id from equipment_status_line_items  where rnrid = v_rnr_id);
DELETE FROM equipment_status_line_items where rnrid = v_rnr_id;
DELETE FROM manual_test_line_item where rnrid= v_rnr_id;
DELETE FROM requisitions WHERE id= in_rnrid;
end if;
RETURN msg;
EXCEPTION WHEN OTHERS THEN
RETURN 'Error in deleting requisition id ' || in_rnrid ||'( '|| SQLERRM || ')';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.fn_delete_rnr(integer)
  OWNER TO postgres;
