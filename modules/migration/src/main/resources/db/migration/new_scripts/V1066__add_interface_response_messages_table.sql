DROP TABLE IF EXISTS interface_response_messages;

CREATE TABLE public.interface_response_messages
(
  id serial,
  status character varying(250) NOT NULL,
  rnrId integer,
  sourceOrderId character varying(250) NOT NULL,
  code character varying(250),
  description character varying(250),
  createddate timestamp without time zone DEFAULT now(),
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT interface_response_messages_pkey PRIMARY KEY (id)
);



DO $$
    BEGIN
        BEGIN
            ALTER TABLE requisitions ADD COLUMN isSent Boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column isSent already exists in isSent.';
        END;
    END;
$$;


/*Add configuration settings for cron job schedule*/

delete from configuration_settings where key = 'INTERFACE_JOB_SCHEDULE';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('INTERFACE_JOB_SCHEDULE','*/15 * * * * ?','Schedule to send data to Facility System','Schedule to send data to Facility Level System','GENERAL','TEXT',true);

delete from configuration_settings where key = 'ELMIS_SDP_USERNAME';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('ELMIS_SDP_USERNAME','elmis','Username to send Feedback to HIM','Username to send Feedback to HIM','GENERAL','TEXT',true);

delete from configuration_settings where key = 'ELMIS_SDP_PASSWORD';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('ELMIS_SDP_PASSWORD','elmis','Password to send Feedback to HIM','Password to send Feedback to HIM','GENERAL','TEXT',true);
