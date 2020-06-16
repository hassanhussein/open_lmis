DELETE FROM WMS_location_types where code ='quarantine';

INSERT into WMS_location_types(code, name, displayOrder) VALUES  ('quarantine', 'Quarantine', 3);
