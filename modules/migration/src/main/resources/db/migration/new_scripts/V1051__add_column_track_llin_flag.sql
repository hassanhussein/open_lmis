DO $$
    BEGIN
        BEGIN
            ALTER TABLE products ADD COLUMN trackNet Boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column trackNet already exists in products.';
        END;
    END;
$$