
ALTER TABLE asn_details
    DROP COLUMN IF EXISTS unitprice;


ALTER TABLE asn_details
    ADD COLUMN unitprice NUMERIC(20,2);