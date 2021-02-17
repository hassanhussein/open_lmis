ALTER TABLE public.vaccine_order_requisitions
        DROP COLUMN IF EXISTS requestNumber;

         ALTER TABLE public.vaccine_order_requisitions
        ADD COLUMN requestNumber character varying(50);


  ALTER TABLE public.vaccine_order_requisitions ADD CONSTRAINT constraint_request_number UNIQUE (requestNumber);
