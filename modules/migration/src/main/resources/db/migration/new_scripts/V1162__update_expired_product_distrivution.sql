ALTER TABLE public.products
    DROP COLUMN IF EXISTS expiredShelfLife ;

    ALTER TABLE public.products
    ADD COLUMN expiredShelfLife integer default 0;
