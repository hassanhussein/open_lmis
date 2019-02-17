DO $$
    BEGIN
        BEGIN
            ALTER TABLE budget_line_items ADD COLUMN additive Boolean default true;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column additive already exists in budget_line_items.';
        END;
    END;
$$;



DO $$
    BEGIN
        BEGIN
            ALTER TABLE budget_line_items ADD COLUMN fundSourceCode character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column fundSourceCode already exists in budget_line_items.';
        END;
    END;
$$;