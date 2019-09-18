ALTER TABLE asns DROP CONSTRAINT IF EXISTS asns_manufacturers_fkey;
ALTER TABLE asns DROP CONSTRAINT IF EXISTS asns_supply_partners_fkey;
ALTER TABLE asns ADD CONSTRAINT asns_supply_partners_fkey FOREIGN KEY (supplierid) REFERENCES supply_partners (ID);