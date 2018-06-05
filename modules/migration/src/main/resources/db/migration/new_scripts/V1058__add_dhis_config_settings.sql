DELETE from configuration_settings where key ='HIM_USERNAME_FOR_DHIS';

INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('HIM_USERNAME_FOR_DHIS','','Username to Access HIM for DHIS2','GENERAL','TEXT');

DELETE from configuration_settings where key ='HIM_PASSWORD_FOR_DHIS';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('HIM_PASSWORD_FOR_DHIS','','Password to Access HIM for DHIS','GENERAL','TEXT');

DELETE from configuration_settings where key ='HIM_DHIS_URL';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('HIM_DHIS_URL','','HIM_DHIS URL to send Immunization DATA ','GENERAL','TEXT');


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


