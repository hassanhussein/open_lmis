DROP TABLE IF EXISTS public.clearing_agents;

CREATE TABLE public.clearing_agents
(
  id serial,
  name character varying(250),
  displayorder integer,
  createddate timestamp without time zone DEFAULT now(),
  CONSTRAINT clearing_agents_pkey PRIMARY KEY (id)
);

INSERT INTO clearing_agents(
            name,displayorder, createdDate)
    VALUES ('MSD' ,1, 'NOW()'),('GPSA',2,'NOW()');