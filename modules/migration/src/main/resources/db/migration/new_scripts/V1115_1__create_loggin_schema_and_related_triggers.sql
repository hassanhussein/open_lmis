/*
drop trigger from core tables if it already applied
*/

DROP TRIGGER IF EXISTS log_change ON public.equipment_category;


DROP TRIGGER IF EXISTS log_change ON public.equipment_cold_chain_equipment_designations;


DROP TRIGGER IF EXISTS log_change ON public.equipment_cold_chain_equipment_pqs_status;


DROP TRIGGER IF EXISTS log_change ON public.equipment_cold_chain_equipments;


DROP TRIGGER IF EXISTS log_change ON public.equipment_energy_types;


DROP TRIGGER IF EXISTS log_change ON public.equipment_functional_test_types;


DROP TRIGGER IF EXISTS log_change ON public.equipment_inventories;


DROP TRIGGER IF EXISTS log_change ON public.equipment_model;


DROP TRIGGER IF EXISTS log_change ON public.equipment_operational_status;





DROP TRIGGER IF EXISTS log_change ON public.equipment_service_types;


DROP TRIGGER IF EXISTS log_change ON public.equipment_service_vendor_users;


DROP TRIGGER IF EXISTS log_change ON public.equipment_service_vendors;






DROP TRIGGER IF EXISTS log_change ON public.equipment_type_programs;


DROP TRIGGER IF EXISTS log_change ON public.equipment_types;


DROP TRIGGER IF EXISTS log_change ON public.equipments;


DROP TRIGGER IF EXISTS log_change ON public.facilities;


DROP TRIGGER IF EXISTS log_change ON public.facility_approved_products;


DROP TRIGGER IF EXISTS log_change ON public.geographic_levels;


DROP TRIGGER IF EXISTS log_change ON public.geographic_zones;


DROP TRIGGER IF EXISTS log_change ON public.losses_adjustments_types;


DROP TRIGGER IF EXISTS log_change ON public.processing_periods;


DROP TRIGGER IF EXISTS log_change ON public.processing_schedules;


DROP TRIGGER IF EXISTS log_change ON public.product_categories;


DROP TRIGGER IF EXISTS log_change ON public.products;


DROP TRIGGER IF EXISTS log_change ON public.program_products;


DROP TRIGGER IF EXISTS log_change ON public.programs;


DROP TRIGGER IF EXISTS log_change ON public.programs_supported;


DROP TRIGGER IF EXISTS log_change ON public.regimen_categories;


DROP TRIGGER IF EXISTS log_change ON public.regimen_combination_constituents;


DROP TRIGGER IF EXISTS log_change ON public.regimen_constituents_dosages;


DROP TRIGGER IF EXISTS log_change ON public.regimen_line_items;


DROP TRIGGER IF EXISTS log_change ON public.regimen_product_combinations;


DROP TRIGGER IF EXISTS log_change ON public.regimens;

/*

*/
 drop TABLE if EXISTS logging.t_history;
drop TABLE if EXISTS logging.t_history_batch;
drop FUNCTION if exists change_trigger();
DROP SEQUENCE IF EXISTS logging.t_history_batch_seq;
DROP SEQUENCE IF EXISTS logging.t_history_id_seq;
-- logging.t_history_id_seq
--------------------------------------------------------------------------------------------------------------------------
drop SCHEMA if exists logging;

-- Extension: hstore

DROP EXTENSION IF EXISTS hstore;

CREATE EXTENSION hstore
    SCHEMA public
    VERSION "1.4";


-----------------------------------------------------------------------------------------------------------------------------
/*


*/



/*


*/
CREATE FUNCTION change_trigger() RETURNS trigger AS $$


BEGIN

        IF      TG_OP = 'INSERT'

        THEN

                INSERT INTO logging.t_history (tabname, schemaname, operation, new_val,sql,raw_data,params)

                VALUES (TG_RELNAME, TG_TABLE_SCHEMA, TG_OP, row_to_json(NEW),current_query(),hstore(NEW.*),TG_ARGV[1]::text[]);

                RETURN NEW;

        ELSIF   TG_OP = 'UPDATE'

                THEN

                        INSERT INTO logging.t_history (tabname, schemaname, operation, new_val, old_val,sql,raw_data,params)

                        VALUES (TG_RELNAME, TG_TABLE_SCHEMA, TG_OP,

                                row_to_json(NEW), row_to_json(OLD),current_query(),hstore(NEW.*),TG_ARGV[1]::text[]);

                        RETURN NEW;

        ELSIF   TG_OP = 'DELETE'

                THEN

                        INSERT INTO logging.t_history (tabname, schemaname, operation, old_val,sql,raw_data,params)

                        VALUES (TG_RELNAME, TG_TABLE_SCHEMA, TG_OP, row_to_json(OLD),current_query(),hstore(OLD.*),TG_ARGV[1]::text[]);

                        RETURN OLD;

        END IF;

END;



$$ LANGUAGE 'plpgsql' SECURITY DEFINER;
/*


*/
CREATE SCHEMA logging;
-----------------------------
CREATE SEQUENCE logging.t_history_batch_seq;

ALTER SEQUENCE logging.t_history_batch_seq
OWNER TO postgres;

-------------------------------------------------------------------

-----------------------------
CREATE SEQUENCE logging.t_history_id_seq;

ALTER SEQUENCE logging.t_history_id_seq
OWNER TO postgres;

-------------------------------------------------------------------


CREATE TABLE logging.t_history_batch
(
        id integer NOT NULL DEFAULT nextval('logging.t_history_batch_seq'::regclass),
        uuid uuid,
        description character varying(1000) COLLATE pg_catalog."default",
        createdby integer,
        createddate timestamp without time zone DEFAULT now(),
        modifiedby integer,
        modifieddate timestamp without time zone DEFAULT now(),
        CONSTRAINT t_history_batch_pkey PRIMARY KEY (id)
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE logging.t_history_batch
        OWNER to postgres;
CREATE TABLE logging.t_history
(
        id integer NOT NULL DEFAULT nextval('logging.t_history_id_seq'::regclass),
        data_change_batch_uuid uuid,
        data_change_batch_id integer,
        tstamp timestamp without time zone DEFAULT now(),
        schemaname text COLLATE pg_catalog."default",
        tabname text COLLATE pg_catalog."default",
        operation text COLLATE pg_catalog."default",
        who text COLLATE pg_catalog."default" DEFAULT "current_user"(),
        new_val json,
        old_val json,
        sql text COLLATE pg_catalog."default",
        raw_data hstore,
        params text[] COLLATE pg_catalog."default"
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE logging.t_history
        OWNER to postgres;

/*



*/
CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_category
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_cold_chain_equipment_designations
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_cold_chain_equipment_pqs_status
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_energy_types
FOR EACH ROW EXECUTE PROCEDURE change_trigger();

CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_functional_test_types
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_inventories
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_model
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_operational_status
FOR EACH ROW EXECUTE PROCEDURE change_trigger();

CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_service_types
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_service_vendor_users
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_service_vendors
FOR EACH ROW EXECUTE PROCEDURE change_trigger();



CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_type_programs
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipment_types
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.equipments
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.facilities
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.facility_approved_products
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.geographic_levels
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.geographic_zones
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.losses_adjustments_types
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.processing_periods
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.processing_schedules
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.product_categories
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.products
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.program_products
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.programs
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.programs_supported
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.regimen_categories
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.regimen_combination_constituents
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.regimen_constituents_dosages
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.regimen_line_items
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.regimen_product_combinations
FOR EACH ROW EXECUTE PROCEDURE change_trigger();


CREATE TRIGGER log_change AFTER
INSERT
OR
UPDATE
OR
DELETE ON public.regimens
FOR EACH ROW EXECUTE PROCEDURE change_trigger();