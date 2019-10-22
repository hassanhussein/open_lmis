DO $$
    BEGIN
        BEGIN
            ALTER TABLE facility_approved_products ADD COLUMN isActive boolean default true;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column isActive already exists in facility_types.';
        END;
    END;
$$;