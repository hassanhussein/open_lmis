DO $$
    BEGIN
        BEGIN
            ALTER TABLE products ADD COLUMN msduom character varying(250);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column msduom already exists in products.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE products ADD COLUMN pricecode character varying(250);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column pricecode already exists in products.';
        END;
    END;
$$;
