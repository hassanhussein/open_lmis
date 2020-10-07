ALTER TABLE public.receive_lots
    DROP COLUMN IF EXISTS campaign;

    ALTER TABLE public.receive_lots
    ADD COLUMN campaign boolean default false;