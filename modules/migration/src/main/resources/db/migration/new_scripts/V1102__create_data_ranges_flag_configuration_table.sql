DROP TABLE if exists public.data_range_flags_configuration;

DROP SEQUENCE if exists  public.data_range_flags_configuration_id_seq;

CREATE SEQUENCE public.data_range_flags_configuration_id_seq;


CREATE TABLE public.data_range_flags_configuration
(
    id integer NOT NULL DEFAULT nextval('data_range_flags_configuration_id_seq'::regclass),
    category character varying(40) COLLATE pg_catalog."default" NOT NULL,
    rangename character varying(100) COLLATE pg_catalog."default" NOT NULL,
    displayname character varying(20) COLLATE pg_catalog."default" NOT NULL,
    minvalue double precision NOT NULL,
    maxvalue double precision NOT NULL,
    description character varying(500) COLLATE pg_catalog."default",
    createdby integer,
    createddate date,
    modifiedby integer,
    modifieddate date,
    CONSTRAINT data_range_flags_configuration_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.data_range_flags_configuration
    OWNER to postgres;