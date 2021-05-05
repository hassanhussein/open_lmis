ALTER TABLE public.vaccine_reports
    DROP COLUMN IF EXISTS lastActive;

ALTER TABLE public.vaccine_reports
   ADD COLUMN lastActive boolean default true;