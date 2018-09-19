DROP TABLE IF EXISTS rejection_reasons;

CREATE TABLE rejection_reasons
(
  id integer NOT NULL DEFAULT nextval('comments_id_seq'::regclass),
  rnrid integer NOT NULL,
  name character varying(250) NOT NULL,
  code character varying(250) NOT NULL,
  createdby integer NOT NULL,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer NOT NULL,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT rejection_reasons_pkey PRIMARY KEY (id),
  CONSTRAINT rejection_reasons_createdby_fkey FOREIGN KEY (createdby)
      REFERENCES public.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT rejection_reasons_modifiedby_fkey FOREIGN KEY (modifiedby)
      REFERENCES public.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT rejection_reasons_rnrid_fkey FOREIGN KEY (rnrid)
      REFERENCES public.requisitions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



 DROP TABLE IF EXISTS public.rejections;

CREATE TABLE public.rejections
(
  id serial,
  name character varying(250) NOT NULL,
  code character varying(250) NOT NULL,
  createdby integer NOT NULL,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer NOT NULL,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT rejections_pkey PRIMARY KEY (id)

)
WITH (
  OIDS=FALSE
);