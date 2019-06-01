

DROP TABLE IF EXISTS  public.budget_line_items;

CREATE TABLE public.budget_line_items
(
  id serial,
  periodid integer,
  budgetfileid integer,
  perioddate timestamp without time zone NOT NULL,
  allocatedbudget numeric(20,2) NOT NULL,
  notes character varying(255),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  facilityid integer,
  programid integer,
  additive boolean DEFAULT true,
  fundsourcecode character varying(200),
  budgetid integer,
  CONSTRAINT budget_line_items_pkey PRIMARY KEY (id)

)











