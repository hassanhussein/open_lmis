DROP TABLE IF exists public.requisition_source_of_funds;

CREATE TABLE public.requisition_source_of_funds
(
  rnrid integer NOT NULL,
  name character varying(250) NOT NULL,
  quantity integer,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT requisition_source_of_funds_pkey PRIMARY KEY (rnrid, name),
  CONSTRAINT requisition_source_of_fund_fkey FOREIGN KEY (rnrid)
      REFERENCES public.requisitions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT requisition_source_of_funds_fkey FOREIGN KEY (name)
      REFERENCES public.source_of_funds (name) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



DO $$
    BEGIN
        BEGIN
            ALTER TABLE requisitions ADD COLUMN totalSources integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column totalSources already exists in requisitions.';
        END;
    END;
$$;


--Updated delete function



-- Function: public.fn_delete_rnr(integer)

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


DO $$
    BEGIN
        BEGIN
            ALTER TABLE source_of_funds ADD COLUMN programid integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column programid already exists in source_of_funds.';
        END;
    END;
$$;