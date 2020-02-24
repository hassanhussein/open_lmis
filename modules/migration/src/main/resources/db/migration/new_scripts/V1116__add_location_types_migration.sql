ALTER TABLE WMS_location_types
    DROP COLUMN IF EXISTS displayOrder;
ALTER TABLE WMS_location_types
    ADD COLUMN displayOrder Integer;

DELETE FROM WMS_location_types;

INSERT into WMS_location_types(code, name, displayOrder) VALUES ('storage', 'Storage Bin', 1), ('receiving', 'Receiving Bin', 2);


ALTER TABLE wms_locations
    DROP COLUMN IF EXISTS active;
ALTER TABLE wms_locations
    ADD COLUMN active boolean default true;