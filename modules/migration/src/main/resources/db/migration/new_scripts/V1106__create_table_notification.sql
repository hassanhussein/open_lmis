-- Table: public.notifications

DROP TABLE if exists public.notifications;
-- SEQUENCE: public.notifications_id_seq

DROP SEQUENCE if exists public.notifications_id_seq;

CREATE SEQUENCE public.notifications_id_seq;

ALTER SEQUENCE public.notifications_id_seq
    OWNER TO postgres;

CREATE TABLE public.notifications
(
    id integer NOT NULL DEFAULT nextval('notifications_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
	 code character varying(50) COLLATE pg_catalog."default" NOT NULL,
	  urgency character varying(50) ,
	   type character varying(50) ,
	 description character varying(550) ,
	 message character varying(500) COLLATE pg_catalog."default" NOT NULL,
  createdby integer NOT NULL,
    createddate timestamp without time zone DEFAULT now(),
    modifiedby integer NOT NULL,
    modifieddate timestamp without time zone DEFAULT now(),
    CONSTRAINT notifications_pkey PRIMARY KEY (id),
    CONSTRAINT notifications_createdby_fkey FOREIGN KEY (createdby)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT notifications_modifiedby_fkey FOREIGN KEY (modifiedby)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.notifications
    OWNER to postgres;
