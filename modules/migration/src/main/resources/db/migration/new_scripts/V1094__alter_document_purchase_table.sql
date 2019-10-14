ALTER TABLE purchase_documents DROP COLUMN IF EXISTS file;
ALTER TABLE purchase_documents DROP COLUMN IF EXISTS asnNumber;
ALTER TABLE purchase_documents ADD COLUMN asnNumber CHARACTER VARYING (50);



DROP TABLE IF EXISTS public.purchase_documents;

CREATE TABLE public.purchase_documents
(
  id SERIAL,
  asnid integer,
  documenttype integer,
  filelocation character varying(1000),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  receiveid integer,
  asnnumber character varying(250),
  CONSTRAINT purchase_documents_pkey PRIMARY KEY (id),
  CONSTRAINT purchase_documents_asns_fkey FOREIGN KEY (asnid)
      REFERENCES public.asns (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT purchase_documents_document_types_fkey FOREIGN KEY (documenttype)
      REFERENCES public.document_types (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT purchase_documents_receives_fkey FOREIGN KEY (receiveid)
      REFERENCES public.receives (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


DROP TABLE IF EXISTs public.documents;

CREATE TABLE public.documents
(
  id serial,
  asnnumber character varying(250),
  documenttype integer,
  filelocation character varying(250),
  CONSTRAINT documents_pkey PRIMARY KEY (id)
)
