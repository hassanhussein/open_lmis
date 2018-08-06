
ALTER TABLE processing_periods DROP COLUMN IF EXISTS reportingPeriod;
ALTER TABLE processing_periods DROP COLUMN IF EXISTS isReporting;
ALTER TABLE processing_periods ADD COLUMN isReporting boolean default false;