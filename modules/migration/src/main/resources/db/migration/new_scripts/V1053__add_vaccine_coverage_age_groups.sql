DROP TABLE IF EXISTS vaccine_report_coverage_line_items_age_groups;
CREATE TABLE vaccine_report_coverage_line_items_age_groups
(
  id SERIAL NOT NULL PRIMARY KEY,
  name varchar(200) NOT NULL,
  description varchar(500) NULL,
  displayOrder INTEGER NOT NULL,

  createdBy INTEGER,
  createdDate DATE DEFAULT NOW(),
  modifiedBy INTEGER,
  modifiedDate DATE DEFAULT NOW(),
  ageCategoryId integer
);

INSERT INTO vaccine_report_coverage_line_items_age_groups
( name, description, displayOrder,ageCategoryId)
VALUES('10 to 32 Weeks', '10 to 31 weeks of age', 1,1),
('6 to 15 Weeks', '6 to 15 weeks of age', 2,2),
('18 Months', '18 Months of age', 3,3),
('< 1 Years', 'Under 1 years of age', 4,4),
('1 > Years', 'Above 1 year of age', 5,4);

INSERT INTO vaccine_vitamin_supplementation_age_groups
( name, description, displayOrder)
VALUES
  ('6 Months', '6 months of age', 3);

ALTER TABLE vaccine_report_coverage_line_items
DROP COLUMN IF EXISTS agegroupid;

ALTER TABLE vaccine_report_coverage_line_items
DROP COLUMN IF EXISTS agegroupname;


DO $$
BEGIN
  ALTER TABLE vaccine_report_coverage_line_items ADD COLUMN agegroupId Integer ;
  EXCEPTION
  WHEN duplicate_column THEN RAISE NOTICE 'column agegroupId already exists in vaccine_report_coverage_line_items.';
END;
$$;

DO $$
BEGIN
  ALTER TABLE vaccine_report_coverage_line_items ADD COLUMN agegroupname character varying(200);
  EXCEPTION
  WHEN duplicate_column THEN RAISE NOTICE 'column agegroupname already exists in vaccine_report_coverage_line_items.';
END;
$$;


ALTER TABLE vaccine_product_doses
  DROP COLUMN IF EXISTS ageCategoryId;
ALTER TABLE vaccine_product_doses
  ADD COLUMN ageCategoryId INTEGER;