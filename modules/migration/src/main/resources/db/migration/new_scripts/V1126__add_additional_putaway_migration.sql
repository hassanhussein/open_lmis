
ALTER TABLE putaway_line_items
    DROP COLUMN IF EXISTS fromBinLocationId;
ALTER TABLE putaway_line_items
    ADD COLUMN fromBinLocationId INteger;

ALTER TABLE putaway_line_items
    DROP COLUMN IF EXISTS toBinLocationId;
ALTER TABLE putaway_line_items
    ADD COLUMN toBinLocationId INteger;



DELETE FROM wms_location_types WHERE CODE ='qurantine';

INSERT INTO public.wms_location_types(
            code, name, displayorder)
    VALUES ('qurantine', 'Qurantine', 6);
