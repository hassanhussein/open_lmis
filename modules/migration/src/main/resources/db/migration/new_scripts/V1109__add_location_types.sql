
ALTER TABLE facilities
    DROP COLUMN IF EXISTS geoLocationCategoryId;
ALTER TABLE facilities
    ADD COLUMN geoLocationCategoryId INTEGER;



    DROP TABLE IF EXISTS public.facility_geo_locations;

    CREATE TABLE public.facility_geo_locations
    (
      id serial,
      code character varying(200),
      name character varying(200),
      CONSTRAINT facility_geo_locations_pkey PRIMARY KEY (id)
    );