DROP TABLE IF EXISTS user_ranks;

CREATE TABLE public.user_ranks
(
  id serial,
  code character varying(50) NOT NULL,
  name character varying(100) NOT NULL,
  displayorder integer ,
  CONSTRAINT user_ranks_pkey PRIMARY KEY (id),
  CONSTRAINT user_ranks_code_key UNIQUE (code)
);



DO $$
    BEGIN
        BEGIN
            ALTER TABLE users ADD COLUMN userrankid integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column userrankid already exists in users.';
        END;
    END;
$$;