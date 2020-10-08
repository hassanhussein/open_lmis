

ALTER TABLE public.vaccine_distribution_line_item_lots
    DROP COLUMN IF EXISTS packsize;

ALTER TABLE public.vaccine_distribution_line_item_lots
    ADD COLUMN  packsize integer default 1;


ALTER TABLE public.vaccine_distribution_line_item_lots
    DROP COLUMN IF EXISTS vvmid;

    ALTER TABLE public.vaccine_distribution_line_item_lots
    ADD COLUMN vvmid integer;


    ALTER TABLE public.vaccine_distribution_line_item_lots
        DROP COLUMN IF EXISTS locationid;

         ALTER TABLE public.vaccine_distribution_line_item_lots
        ADD COLUMN locationid integer;

        ALTER TABLE public.vaccine_distribution_line_item_lots
            DROP COLUMN IF EXISTS stockcardid;

        ALTER TABLE public.vaccine_distribution_line_item_lots
            ADD COLUMN stockcardid integer;


         ALTER TABLE public.vaccine_distribution_line_items
             DROP COLUMN gap;

         ALTER TABLE public.vaccine_distribution_line_items
             ADD COLUMN gap integer;

