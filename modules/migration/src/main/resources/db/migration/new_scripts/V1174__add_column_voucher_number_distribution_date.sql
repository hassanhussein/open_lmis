ALTER TABLE public.vaccine_order_requisitions
        DROP COLUMN IF EXISTS voucherNumber;

         ALTER TABLE public.vaccine_order_requisitions
        ADD COLUMN voucherNumber character varying(100);

ALTER TABLE public.vaccine_order_requisitions
        DROP COLUMN IF EXISTS distributionDate;

         ALTER TABLE public.vaccine_order_requisitions
        ADD COLUMN distributionDate timestamp without time zone;


ALTER TABLE public.vaccine_order_requisitions
        DROP COLUMN IF EXISTS distributionType;

         ALTER TABLE public.vaccine_order_requisitions
        ADD COLUMN distributionType character varying(30);

ALTER TABLE public.lots ALTER COLUMN packsize SET DEFAULT '1';
ALTER TABLE public.inspection_lots ALTER COLUMN failquantity SET DEFAULT 0;
