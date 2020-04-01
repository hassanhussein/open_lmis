DO $$
    BEGIN
        BEGIN
            ALTER TABLE program_products ADD COLUMN isCovidIndicator Boolean DEFAULT FALSE;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column isCovidIndicator already exists in products.';
        END;
    END;
$$;





DROP TABLE IF EXISTS public.monitoring_report_line_items;

DROP TABLE IF EXISTS public.monitoring_report_status_changes;


DROP TABLE if exists public.monitoring_reports;

CREATE TABLE public.monitoring_reports
(
  id serial,
  districtId integer NOT NULL,
  programId Integer,
  supervisoryNodeId integer,
  status character varying(250),
  nameOfHIDTU character varying(250),
  numberOfHIDTU character varying(250),
  numberOfCumulativeCases integer,
  patientOnTreatment integer,
  numberOfStaff integer,
  reportedDate timestamp without time zone DEFAULT now(),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),

  CONSTRAINT monitoring_reports_pkey PRIMARY KEY (id)
);

CREATE TABLE public.monitoring_report_line_items
(
  id serial,
  reportId integer NOT NULL,
  productId integer NOT NULL,
  product character varying(250),
  productCode character varying(250),
  productCategoryId integer NOT NULL,
  dispensingUnit character varying(250),
  packSize INTEGER,
  stockOnHand Integer,
  quantityRequested integer,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),

  CONSTRAINT monitoring_report_line_items_pkey PRIMARY KEY (id),

  CONSTRAINT  monitoring_report_line_items_productid_fkey FOREIGN KEY (productid)
      REFERENCES public.products (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,

    CONSTRAINT  monitoring_report_line_items_reportId_fkey FOREIGN KEY (reportId)
      REFERENCES public.monitoring_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.monitoring_report_status_changes
(
  id serial,
  reportid integer NOT NULL,
  status character varying(50) NOT NULL,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  comment character varying(1000),
  CONSTRAINT monitoring_report_status_changes_pkey PRIMARY KEY (id),
  CONSTRAINT monitoring_report_status_changes_reportid_fkey FOREIGN KEY (reportid)
      REFERENCES public.monitoring_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


DELETE from role_rights where rightName = 'CREATE_MONITORING_REPORT';
DELETE FROM rights where name = 'CREATE_MONITORING_REPORT';
INSERT INTO rights (name, rightType, displayNameKey, displayOrder, description) VALUES
('CREATE_MONITORING_REPORT','REQUISITION','right.create.monitoring.report', 107,'Permission to Create Monitoring Report');