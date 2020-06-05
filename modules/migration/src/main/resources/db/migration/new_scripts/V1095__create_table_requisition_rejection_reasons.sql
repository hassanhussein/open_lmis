-- Table: public.requisition_rejection_reasons

DROP TABLE if exists public.requisition_rejection_reasons;
DROP SEQUENCE if exists public.requisition_rejection_reasons_id_seq1;

CREATE SEQUENCE public.requisition_rejection_reasons_id_seq1;
CREATE TABLE public.requisition_rejection_reasons
(
    id integer NOT NULL DEFAULT nextval('requisition_rejection_reasons_id_seq1'::regclass),
    code character varying(10) COLLATE pg_catalog."default" NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(100) COLLATE pg_catalog."default",
    regular_rnr boolean NOT NULL,
    emergency_rnr boolean NOT NULL,
    createdby integer,
    modifiedby integer,
    createddate date,
    modifieddate date,
    CONSTRAINT requisition_rejection_reasons_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.requisition_rejection_reasons
    OWNER to postgres;