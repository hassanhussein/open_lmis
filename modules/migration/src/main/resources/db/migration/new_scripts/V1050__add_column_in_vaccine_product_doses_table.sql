DO $$
BEGIN
  ALTER TABLE vaccine_product_doses ADD COLUMN displayInDashboard boolean DEFAULT false;
  EXCEPTION
  WHEN duplicate_column THEN RAISE NOTICE 'column displayInDashboard already exists in Vaccine_product_doses.';
END;
$$;

DO $$
BEGIN
  ALTER TABLE vaccine_product_doses ADD COLUMN dashboardDisplayOrder integer;
  EXCEPTION
  WHEN duplicate_column THEN RAISE NOTICE 'column dashboardDisplayOrder already exists in Vaccine_product_doses.';
END;
$$;