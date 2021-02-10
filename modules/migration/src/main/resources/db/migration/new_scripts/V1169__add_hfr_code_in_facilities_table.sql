DO $$
    BEGIN
        BEGIN
            ALTER TABLE facilities ADD COLUMN hfrCode CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column hfrCode already exists in facilities.';
        END;
    END;
$$;



DROP TABLE IF EXISTS public.elmis_hfr_districts;

CREATE TABLE public.elmis_hfr_districts
(
  id serial,
  code character varying(50) NOT NULL,
  hfrCode character varying(200) NOT NULL,
  description character varying(5000) NOT NULL,
  CONSTRAINT elmis_hfr_districts_pkey PRIMARY KEY (id)
);


DO $$
    BEGIN
        BEGIN
            ALTER TABLE hfr_facilities ADD COLUMN msdCode CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column msdCode already exists in facilities.';
        END;
    END;
$$;


DO $$
    BEGIN
        BEGIN
            ALTER TABLE hfr_facilities ADD COLUMN activatedByMsd boolean DEFAULT false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column msdCode already exists in facilities.';
        END;
    END;
$$;


DO $$
    BEGIN
        BEGIN
            ALTER TABLE hfr_facilities ADD COLUMN activatedDate CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column activatedDate already exists in hfr_facilities.';
        END;
    END;
$$;



