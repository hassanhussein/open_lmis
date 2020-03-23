DELETE FROM wms_locations a
WHERE a.id <> (SELECT min(b.id)
                 FROM   wms_locations b
                 WHERE  a.code = b.code);


DELETE FROM warehouses a
WHERE a.id <> (SELECT min(b.id)
                 FROM   warehouses b
                 WHERE  a.code = b.code);


DELETE FROM sites a
WHERE a.id <> (SELECT min(b.id)
                 FROM   sites b
                 WHERE  a.code = b.code);

ALTER TABLE sites DROP CONSTRAINT IF EXISTS site_unique ;
ALTER TABLE sites ADD CONSTRAINT site_unique UNIQUE (code);

ALTER TABLE warehouses DROP CONSTRAINT IF EXISTS warehouses_unique;
ALTER TABLE warehouses ADD CONSTRAINT warehouses_unique UNIQUE (code);


DELETE FROM wms_location_types WHERE CODE ='qurantine';

INSERT INTO public.wms_location_types(
            code, name, displayorder)
    VALUES ('qurantine', 'Qurantine', 3);


 ALTER TABLE wms_locations DROP CONSTRAINT IF EXISTS wms_locations_unique;
 ALTER TABLE wms_locations ADD CONSTRAINT wms_locations_unique UNIQUE (code);


