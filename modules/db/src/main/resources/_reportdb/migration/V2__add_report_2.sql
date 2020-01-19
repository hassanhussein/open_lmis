DROP TABLE IF EXISTS public.stock_imbalance_report_2;

CREATE TABLE public.stock_imbalance_report_2
(
  id serial,
  name character varying(50) NOT NULL,
  description character varying(250),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT stock_imbalance_report_2_pkey PRIMARY KEY (id)
)