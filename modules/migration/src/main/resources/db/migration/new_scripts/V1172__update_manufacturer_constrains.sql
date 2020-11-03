
DO $$
    BEGIN
        BEGIN
            ALTER TABLE manufacturers  ADD COLUMN code character varying (200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column code already exists in manufacturers';
        END;
    END;
$$;
