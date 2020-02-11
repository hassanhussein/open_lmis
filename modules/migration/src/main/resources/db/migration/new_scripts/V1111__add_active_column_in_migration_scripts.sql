ALTER TABLE warehouses
    DROP COLUMN IF EXISTS active;
ALTER TABLE warehouses
    ADD COLUMN active boolean DEFAULT true;