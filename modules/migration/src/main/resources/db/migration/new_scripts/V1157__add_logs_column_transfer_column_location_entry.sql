ALTER TABLE public.lot_location_entries
    DROP COLUMN IF EXISTS istransferred;

    ALTER TABLE public.lot_location_entries
    ADD COLUMN istransferred boolean default false;



      ALTER TABLE public.lot_location_entries
        DROP COLUMN IF EXISTS transferlogs;

         ALTER TABLE public.lot_location_entries
        ADD COLUMN transferlogs character varying(100);