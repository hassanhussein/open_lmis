ALTER TABLE asn_lots
    DROP COLUMN IF EXISTS unitprice;


ALTER TABLE asn_lots
    ADD COLUMN unitprice NUMERIC(20,2);