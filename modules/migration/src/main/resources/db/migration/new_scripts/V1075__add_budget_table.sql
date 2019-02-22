DROP TABLE IF EXISTS budgets;

CREATE TABLE public.budgets
(
  id serial,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  facilityid integer NOT NULL,
  sourceApplication character varying(100),
  receiveddate timestamp without time zone DEFAULT now(),
  CONSTRAINT budgets_pkey PRIMARY KEY (id),
  CONSTRAINT budgets_facilityid_fkey FOREIGN KEY (facilityid)
      REFERENCES public.facilities (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

);


DO $$
    BEGIN
        BEGIN
            ALTER TABLE budget_line_items ADD COLUMN budgetId integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column budgetId already exists in budget_line_items.';
        END;
    END;
$$;
