INSERT INTO public.dosage_units(id,
            code, displayOrder, createdDate)
            VALUES ('18','Vials',18,now());

ALTER TABLE public.products
        DROP COLUMN IF EXISTS wmsDosageUnitId;
         ALTER TABLE public.products
        ADD COLUMN wmsDosageUnitId integer;


