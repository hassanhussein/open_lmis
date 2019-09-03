

DROP TABLE IF EXISTS wms_documents;
CREATE TABLE wms_documents
(
  id serial NOT NULL,
  document_type character varying(20),
  url character varying(200),
  createddate date,
  modifieddate date,
  createdby integer,
  modifiedby integer,
  CONSTRAINT wms_documents_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE wms_documents
  OWNER TO postgres;
