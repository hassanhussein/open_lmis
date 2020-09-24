ALTER TABLE public.receive_lots
    ADD COLUMN IF NOT EXISTS campaign boolean default false;