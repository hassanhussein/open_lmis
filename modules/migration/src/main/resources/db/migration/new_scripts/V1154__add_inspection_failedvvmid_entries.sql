ALTER TABLE public.inspection_lots
    DROP COLUMN IF EXISTS failvvmid ;

    ALTER TABLE public.inspection_lots
    ADD COLUMN failvvmid integer;