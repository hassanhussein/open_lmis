DROP TABLE IF EXISTs public.documents;

CREATE TABLE public.documents
(
  id serial,
  asnnumber character varying(250),
  documenttype integer,
  filelocation character varying(250),
  CONSTRAINT documents_pkey PRIMARY KEY (id)
)
