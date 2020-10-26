DROP TABLE if exists public.facility_notifications;
DROP SEQUENCE if exists public.facility_notifications_id_seq;
---------------------------------------------------------------
-----------------------------------------------------------

CREATE SEQUENCE public.facility_notifications_id_seq;

ALTER SEQUENCE public.facility_notifications_id_seq
    OWNER TO postgres;

	------------------------------------------------------------------------


CREATE TABLE public.facility_notifications
(
    id integer NOT NULL DEFAULT nextval('facility_notifications_id_seq'::regclass),
    facilityid integer ,
    facilitycode character varying(50) COLLATE pg_catalog."default" NOT NULL,
    notificationid integer NOT NULL,
    downloaded BOOLEAN NOT NULL,
    acknowledged BOOLEAN DEFAULT false,
    createdby integer NOT NULL,
    createddate timestamp without time zone DEFAULT now(),
    modifiedby integer NOT NULL,
    modifieddate timestamp without time zone DEFAULT now(),
    CONSTRAINT facility_notifications_pkey PRIMARY KEY (id),
	CONSTRAINT facility_notifications_id_fkey FOREIGN KEY (facilityid)
        REFERENCES public.facilities (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT facility_notifications_code_fkey FOREIGN KEY (facilitycode)
        REFERENCES public.facilities (code) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT notifications_id_fkey FOREIGN KEY (notificationid)
        REFERENCES public.notifications (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
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

ALTER TABLE public.facility_notifications
    OWNER to postgres;