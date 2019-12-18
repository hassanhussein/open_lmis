DO $$
    BEGIN
        BEGIN
            ALTER TABLE budget_line_items ADD COLUMN creditValue CHARACTER VARYING (50) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column creditValue already exists in budget_line_items.';
        END;
    END;
$$;


DO $$
    BEGIN
        BEGIN
            ALTER TABLE requisitions ADD COLUMN creditValue CHARACTER VARYING (50) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column creditValue already exists in requisitions.';
        END;
    END;
$$;