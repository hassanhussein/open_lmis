
DROP TABLE IF EXISTS public.wms_status_changes;

CREATE TABLE public.wms_status_changes
(
  id serial,
  inspectionId integer NOT NULL,
  status character varying(50) NOT NULL,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT vwms_status_changes_inspection_pkey PRIMARY KEY (id)
);