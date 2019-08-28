-- Table: public.user_dashboard_preferences

DROP TABLE if exists public.user_dashboard_preferences;
DROP SEQUENCE if exists public.user_dashboard_id_seq;

CREATE SEQUENCE public.user_dashboard_id_seq;

ALTER SEQUENCE public.user_dashboard_id_seq
    OWNER TO postgres;

CREATE TABLE public.user_dashboard_preferences
(
    id integer NOT NULL DEFAULT nextval('user_dashboard_id_seq'::regclass),
    dashboard character varying(200) COLLATE pg_catalog."default" NOT NULL,
	userid integer,
    show boolean,
    createdby integer,
    createddate timestamp without time zone DEFAULT now(),
    modifiedby integer,
    modifieddate timestamp without time zone DEFAULT now(),
    CONSTRAINT dashboard_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_dashboard_preferences
    OWNER to postgres;