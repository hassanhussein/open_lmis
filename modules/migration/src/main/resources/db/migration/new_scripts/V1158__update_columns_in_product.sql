DO $$
    BEGIN
        BEGIN
            ALTER TABLE products ADD COLUMN owner character varying(250);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column owner already exists in products.';
        END;
    END;
$$;

