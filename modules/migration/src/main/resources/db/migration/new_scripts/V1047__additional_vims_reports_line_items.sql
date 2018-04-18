
DROP TABLE IF EXISTS pmtct_line_Items;

DROP TABLE IF EXISTS pmtct_categories;

CREATE TABLE pmtct_categories
(
  id serial,
  name character varying(200) NOT NULL,
  description character varying(500),
  displayorder integer NOT NULL,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  CONSTRAINT pmtct_categories_pkey PRIMARY KEY (id)
);

CREATE TABLE pmtct_line_Items
(
  id serial,
  reportid integer NOT NULL,
  transferredToCtc BOOLEAN DEFAULT FALSE,
  categoryId INTEGER,
  category character varying(200),
  malevalue integer,
  femalevalue integer,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  skipped boolean NOT NULL DEFAULT false,
  CONSTRAINT pmtct_line_Items_pkey PRIMARY KEY (id),
  CONSTRAINT pmtct_line_Items_reportid_fkey FOREIGN KEY (reportid)
  REFERENCES public.vaccine_reports (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT pmtct_line_Items_categories_fkey FOREIGN KEY (categoryId)
  REFERENCES pmtct_categories (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS vaccine_report_child_visit_line_items;
DROP TABLE IF EXISTS vaccine_child_visit_age_groups;

CREATE TABLE vaccine_child_visit_age_groups
(
  id serial,
  name character varying(200) NOT NULL,
  description character varying(500),
  displayorder integer NOT NULL,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  CONSTRAINT vaccine_child_visit_age_groups_pkey PRIMARY KEY (id)
);

CREATE TABLE vaccine_report_child_visit_line_items
(
  id serial,
  reportid integer NOT NULL,
  childvisitagegroupid integer NOT NULL,
  ageGroup character varying(200),
  malevalue integer,
  femalevalue integer,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  skipped boolean NOT NULL DEFAULT false,
  CONSTRAINT vaccine_report_child_visit_line_items_pkey PRIMARY KEY (id),
  CONSTRAINT vaccine_report_child_visit_age_group_fkey FOREIGN KEY (childvisitagegroupid)
      REFERENCES vaccine_child_visit_age_groups (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT vaccine_report_child_visit_line_items_reportid_fkey FOREIGN KEY (reportid)
      REFERENCES vaccine_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS mosquito_net_status_line_Items;

CREATE TABLE mosquito_net_status_line_Items
(
  id serial,
  reportid integer NOT NULL,
  malevalue integer,
  femalevalue integer,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  skipped boolean NOT NULL DEFAULT false,
  CONSTRAINT mosquito_net_status_lineItems_pkey PRIMARY KEY (id),
  CONSTRAINT mosquito_net_status_lineItems_reportid_fkey FOREIGN KEY (reportid)
  REFERENCES vaccine_reports (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);



DROP TABLE IF EXISTS tt_Status_Line_Items;
DROP TABLE IF EXISTS tt_status_categories;


CREATE TABLE tt_status_categories
(
  id serial,
  name character varying(200) NOT NULL,
  description character varying(500),
  displayorder integer NOT NULL,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  CONSTRAINT tt_status_categories_pkey PRIMARY KEY (id)
);

CREATE TABLE tt_Status_Line_Items
(
  id serial,
  reportid integer NOT NULL,
  categoryId INTEGER,
  category character varying(200),
  malevalue integer,
  femalevalue integer,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  skipped boolean NOT NULL DEFAULT false,
  CONSTRAINT tt_Status_Line_Items_pkey PRIMARY KEY (id),
  CONSTRAINT tt_Status_Line_Items_reportid_fkey FOREIGN KEY (reportid)
      REFERENCES vaccine_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tt_Status_Line_Items_categories_fkey FOREIGN KEY (categoryId)
      REFERENCES tt_status_categories (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


DROP TABLE IF EXISTS breast_feeding_line_items;

DROP TABLE IF EXISTS breast_feeding_age_groups;
DROP TABLE IF EXISTS breast_feeding_categories;

CREATE TABLE breast_feeding_categories
(
  id serial,
  name character varying(200) NOT NULL,
  description character varying(500),
  displayorder integer NOT NULL,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  CONSTRAINT breast_feeding_categories_pkey PRIMARY KEY (id)
);

CREATE TABLE breast_feeding_age_groups
(
  id serial,
  name character varying(200) NOT NULL,
  description character varying(500),
  displayorder integer NOT NULL,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  CONSTRAINT breast_feeding_age_groups_pkey PRIMARY KEY (id)
);


CREATE TABLE breast_feeding_line_items
(
  id serial,
  reportid integer NOT NULL,
  categoryId INTEGER,
  category character varying(200),
  ageGroupId INTEGER,
  ageGroup character varying (200),
  malevalue integer,
  femalevalue integer,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  skipped boolean NOT NULL DEFAULT false,
  CONSTRAINT breast_feeding_line_items_pkey PRIMARY KEY (id),
  CONSTRAINT breast_feeding_line_items_reportid_fkey FOREIGN KEY (reportid)
      REFERENCES vaccine_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
       CONSTRAINT breast_feeding_age_groups_agegroupid_fkey FOREIGN KEY (agegroupid)
      REFERENCES breast_feeding_age_groups (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT breast_feeding_categories_categories_fkey FOREIGN KEY (categoryId)
      REFERENCES breast_feeding_categories (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


DROP TABLE IF EXISTS vaccine_report_weight_ratio_line_items;
DROP TABLE IF EXISTS weight_ratio_age_groups;
DROP TABLE IF EXISTS weight_ratio_categories;


CREATE TABLE weight_ratio_categories
(
  id serial,
  name character varying(200) NOT NULL,
  description character varying(500),
  displayorder integer NOT NULL,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  CONSTRAINT weight_ratio_categories_pkey PRIMARY KEY (id)
);

CREATE TABLE weight_ratio_age_groups
(
  id serial,
  name character varying(200) NOT NULL,
  description character varying(500),
  displayorder integer NOT NULL,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  CONSTRAINT weight_ratio_age_groups_pkey PRIMARY KEY (id)
);

CREATE TABLE vaccine_report_weight_ratio_line_items
(
  id serial,
  reportid integer NOT NULL,
  agegroupid integer,
  ageGroup character varying (200),
  categoryId integer NOT NULL,
  category character varying (200),
  malevalue integer,
  femalevalue integer,
  createdby integer,
  createddate date DEFAULT now(),
  modifiedby integer,
  modifieddate date DEFAULT now(),
  skipped boolean NOT NULL DEFAULT false,
  CONSTRAINT vaccine_report_weight_ratio_line_items_pkey PRIMARY KEY (id),
  CONSTRAINT weight_ratio_age_group_fkey FOREIGN KEY (agegroupid)
      REFERENCES weight_ratio_age_groups (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT vaccine_report_weight_ratio_line_items_reportid_fkey FOREIGN KEY (reportid)
      REFERENCES vaccine_reports (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT vaccine_report_weight_ratio_category_fkey FOREIGN KEY (categoryId)
      REFERENCES weight_ratio_categories (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
