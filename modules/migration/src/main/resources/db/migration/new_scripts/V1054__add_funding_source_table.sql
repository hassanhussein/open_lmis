DROP TABLE IF EXISTS source_of_funds;

CREATE TABLE source_of_funds
(
  id serial,
  name character varying(50) NOT NULL,
  displayorder integer,
  createddate timestamp without time zone DEFAULT now(),
  CONSTRAINT source_of_funds_pkey PRIMARY KEY (id),
  CONSTRAINT source_of_funds_name_key UNIQUE (name)
);


DROP TABLE IF EXISTS requisition_funding_source_line_items;

CREATE TABLE requisition_funding_source_line_items
(
  id serial,
  rnrid integer NOT NULL,
  allocatedbudget numeric(20,2) NOT NULL,
  createdby integer NOT NULL,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer NOT NULL,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT requisition_funding_sources_pkey PRIMARY KEY (id),
  CONSTRAINT requisition_funding_sources_rnrid_fkey FOREIGN KEY (rnrid)
      REFERENCES public.requisitions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);