DROP TABLE IF EXISTS public.equipment_maintenance_request_reasons;

CREATE TABLE public.equipment_maintenance_request_reasons
(
  id serial,
  code character varying(200),
  name character varying(200)
);

DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_maintenance_requests  ADD COLUMN reasonId  integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column reasonId already exists in equipment_maintenance_requests';
        END;
    END;
$$;
