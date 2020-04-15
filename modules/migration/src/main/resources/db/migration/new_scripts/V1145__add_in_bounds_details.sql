DROP TABLE IF EXISTS public.in_bound_details;

DROP TABLE IF EXISTS public.in_bounds;


CREATE TABLE public.in_bound_details
(
  id serial,
  productcode character varying(200),
  productname character varying(200),
  uom character varying(200),
  quantityordered integer,
  source character varying(200),
  fundvalues numeric(15,2),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT in_bound_details_pkey PRIMARY KEY (id)
);

DO $$
    BEGIN
        BEGIN
            ALTER TABLE in_bound_details ADD COLUMN expectedarrivaldate timestamp without time zone DEFAULT NOW();
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column expectedarrivaldate already exists in in_bound_details.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE in_bound_details ADD COLUMN receivinglocationcode CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column receivinglocationcode already exists in in_bound_details.';
        END;
    END;
$$;






