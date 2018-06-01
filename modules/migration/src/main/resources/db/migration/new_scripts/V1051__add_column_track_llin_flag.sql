DO $$
    BEGIN
        BEGIN
            ALTER TABLE products ADD COLUMN trackNet Boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column trackNet already exists in products.';
        END;
    END;
$$;


DELETE from configuration_settings where key ='LLIN_USERNAME';

INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('LLIN_USERNAME','','Username to Access DHIS for LLIN','GENERAL','TEXT');

DELETE from configuration_settings where key ='LLIN_PASSWORD';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('LLIN_PASSWORD','','Password to Access DHIS for LLIN','GENERAL','TEXT');

DELETE from configuration_settings where key ='LLIN_DHIS_URL';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('LLIN_DHIS_URL','','DHIS URL to send LLIN DATA ','GENERAL','TEXT');


DROP TABLE IF EXISTS public.interface_responses;

CREATE TABLE public.interface_responses
(
  id serial,
  responseType character varying(100) ,
  status character varying(200) ,
  description character varying(200),
  imported integer,
  updated integer,
  ignored integer,
  deleted character varying(200) ,
  affectedobject character varying(200) ,
  affectedvalue character varying(200),
  dataSetComplete boolean default false,
  CONSTRAINT interface_responses_pkey PRIMARY KEY (id)
);


