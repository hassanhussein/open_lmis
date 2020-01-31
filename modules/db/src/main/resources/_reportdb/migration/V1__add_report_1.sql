DROP TABLE IF EXISTS public.stock_imbalance_report;

CREATE TABLE public.stock_imbalance_report
(
  id serial,
  name character varying(50) NOT NULL,
  description character varying(250),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT stock_imbalance_report_pkey PRIMARY KEY (id)
)