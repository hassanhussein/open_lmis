ALTER TABLE public.lot_location_entries
        DROP COLUMN IF EXISTS movementtype;

         ALTER TABLE public.lot_location_entries
        ADD COLUMN movementtype character varying(200);