


ALTER TABLE locations
    DROP COLUMN IF EXISTS scrap;

ALTER TABLE locations
    ADD COLUMN scrap BOOLEAN;

