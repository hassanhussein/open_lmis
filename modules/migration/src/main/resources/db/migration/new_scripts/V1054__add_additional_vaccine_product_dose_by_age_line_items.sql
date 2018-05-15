

 DROP TABLE IF EXISTS vaccine_product_dose_age_groups;

CREATE TABLE vaccine_product_dose_age_groups
(
  id serial,
  agegroupId INTEGER,
  ageGroupName character varying(200),
  doseid integer NOT NULL,
  programid integer NOT NULL,
  productid integer NOT NULL,
  displayorder integer NOT NULL,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT vaccine_product_dose_age_groups_PKEY PRIMARY KEY (id),
  CONSTRAINT vaccine_product_dose_age_groups_doseid_fkey FOREIGN KEY (doseid)
      REFERENCES public.vaccine_doses (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT vaccine_product_dose_age_groups_productid_fkey FOREIGN KEY (productid)
      REFERENCES public.products (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT vaccine_product_dose_age_groups_programid_fkey FOREIGN KEY (programid)
      REFERENCES public.programs (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



DROP TABLE IF EXISTS vaccine_report_coverage_age_group_line_items;

CREATE TABLE vaccine_report_coverage_age_group_line_items
(
  id serial,
  skipped boolean NOT NULL DEFAULT false,
  reportid integer NOT NULL,
  productid integer NOT NULL,
  doseid integer NOT NULL,
  displayorder integer NOT NULL,
  displayname character varying(100) NOT NULL,
  trackmale boolean NOT NULL DEFAULT true,
  trackfemale boolean NOT NULL DEFAULT true,
  regularmale integer,
  regularfemale integer,
  outreachmale integer,
  outreachfemale integer,
  campaignmale integer,
  campaignfemale integer,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  agegroupid integer,
  agegroupname character varying(200),
  CONSTRAINT vaccine_report_coverage_age_group_line_items_pkey PRIMARY KEY (id),
  CONSTRAINT vaccine_report_coverage_age_group_line_items_doseid_fkey FOREIGN KEY (doseid)
      REFERENCES public.vaccine_doses (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT vaccine_report_coverage_age_group_line_items_productid_fkey FOREIGN KEY (productid)
      REFERENCES public.products (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT vaccine_report_coverage_age_group_line_items_reportid_fkey FOREIGN KEY (reportid)
      REFERENCES public.vaccine_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)