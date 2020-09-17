ALTER TABLE public.inspection_lots
    ADD COLUMN IF NOT EXISTS failvvmid integer;
