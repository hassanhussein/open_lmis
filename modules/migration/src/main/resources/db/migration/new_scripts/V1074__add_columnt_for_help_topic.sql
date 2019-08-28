-- Column: public.elmis_help_topic.key

 ALTER TABLE public.elmis_help_topic DROP COLUMN IF EXISTS key;

ALTER TABLE public.elmis_help_topic
    ADD COLUMN key character varying(100) COLLATE pg_catalog."default";