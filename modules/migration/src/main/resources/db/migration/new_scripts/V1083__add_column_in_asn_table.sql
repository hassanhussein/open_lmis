ALTER TABLE asns
    DROP COLUMN IF EXISTS expecteddeliverydate;


ALTER TABLE asns
    ADD COLUMN expecteddeliverydate TIMESTAMP;