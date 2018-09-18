DO $$
    BEGIN
        BEGIN
            ALTER TABLE programs ADD COLUMN enableMonthlyReporting Boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column enableMonthlyReporting already exists in products.';
        END;
    END;
$$;