DELETE FROM inspection_lots;
DELETE FROM inspection_line_items;
DELETE FROM inspections;
DELETE FROM purchase_documents;
DELETE FROM receive_lots;
delete from asn_lots;
DELETE from asn_details;
DELETE FROM receive_line_items;
DELETE FROM receives;
DELETE FROM asns;

ALTER TABLE asns DROP CONSTRAINT IF EXISTS asn_unique_key;
ALTER TABLE receives DROP CONSTRAINT IF EXISTS receives_unique_key;
ALTER TABLE asns ADD CONSTRAINT asn_unique_key UNIQUE (poNumber);
ALTER TABLE receives ADD CONSTRAINT receives_unique_key UNIQUE (poNumber);

