ALTER TABLE Facilities
    DROP COLUMN IF EXISTS allowedTosend;

ALTER TABLE Facilities
    ADD COLUMN allowedTosend boolean NOT NULL DEFAULT false;

