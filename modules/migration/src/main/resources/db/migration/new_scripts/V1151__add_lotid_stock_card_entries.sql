
 ALTER TABLE public.stock_card_entries
    DROP COLUMN IF EXISTS lotid;

ALTER TABLE public.stock_card_entries
    ADD COLUMN lotid integer;
