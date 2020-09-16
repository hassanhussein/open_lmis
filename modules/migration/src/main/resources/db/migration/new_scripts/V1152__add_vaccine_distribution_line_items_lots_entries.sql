

ALTER TABLE public.vaccine_distribution_line_item_lots
    ADD COLUMN IF NOT EXISTS packsize integer default 1;


ALTER TABLE public.vaccine_distribution_line_item_lots
    ADD COLUMN IF NOT EXISTS vvmid integer;


    ALTER TABLE public.vaccine_distribution_line_item_lots
        ADD COLUMN IF NOT EXISTS locationid integer;


        ALTER TABLE public.vaccine_distribution_line_item_lots
            ADD COLUMN IF NOT EXISTS stockcardid integer;

    ALTER TABLE public.vaccine_distribution_line_items
             DROP COLUMN IF EXISTS gap;

         ALTER TABLE public.vaccine_distribution_line_items
             ADD COLUMN IF NOT EXISTS gap integer;