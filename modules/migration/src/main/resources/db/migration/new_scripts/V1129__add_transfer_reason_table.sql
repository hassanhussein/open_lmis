DROP TABLE IF EXISTS public.wms_reasons;

CREATE TABLE public.wms_reasons
(
  id serial,
  code text NOT NULL,
  reasonName text NOT NULL,
  CONSTRAINT wms_reasons_pkey PRIMARY KEY (id)
);