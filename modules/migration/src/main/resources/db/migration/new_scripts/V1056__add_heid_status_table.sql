DROP TABLE IF EXISTS heid_line_items;

CREATE TABLE heid_line_items
(
  id serial,
  reportid integer NOT NULL,
  malevalue integer,
  femalevalue integer,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  skipped boolean NOT NULL DEFAULT false,
  CONSTRAINT heid_line_items_pkey PRIMARY KEY (id),
  CONSTRAINT heid_line_items_reportid_fkey FOREIGN KEY (reportid)
      REFERENCES public.vaccine_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);