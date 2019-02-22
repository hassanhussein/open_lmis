ALTER TABLE rejection_reasons DROP COLUMN IF EXISTS rnrId;
ALTER TABLE rejection_reasons ADD COLUMN rnrId integer NULL