DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_category  ADD COLUMN disciplineId  integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column disciplineId already exists in equipment_category.';
        END;
    END;
$$;

DROP TABLE IF EXISTS disciplines;

CREATE TABLE disciplines
(
	id serial,
	code character varying(250) NOT NULL,
	name text,
	description text,
	CONSTRAINT disciplines_pk PRIMARY KEY (id),
	CONSTRAINT disciplines_code UNIQUE (code)
);