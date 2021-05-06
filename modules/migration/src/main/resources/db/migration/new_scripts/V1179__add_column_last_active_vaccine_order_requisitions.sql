ALTER TABLE public.vaccine_order_requisitions
    DROP COLUMN IF EXISTS lastActive;

ALTER TABLE public.vaccine_order_requisitions
   ADD COLUMN lastActive boolean default true;