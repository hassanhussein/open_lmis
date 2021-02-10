ALTER TABLE public.lot_location_entries
        DROP COLUMN IF EXISTS reason;

         ALTER TABLE public.lot_location_entries
        ADD COLUMN reason character varying(200);