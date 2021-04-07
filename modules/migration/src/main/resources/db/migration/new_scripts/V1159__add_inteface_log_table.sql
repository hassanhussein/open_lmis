DROP TABLE IF EXISTS interface_logs;

CREATE TABLE public.interface_logs
(
  id serial,
  details character varying(3000),
  isSent boolean default false,
  fileName character varying(200),
  statusCode character varying(200),
  createdDate timestamp without time zone DEFAULT now(),
  CONSTRAINT interface_logs_pkey PRIMARY KEY (id)
);