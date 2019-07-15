DO $$
    BEGIN
        BEGIN
            ALTER TABLE facility_types ADD COLUMN isPrimaryHealthCare boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column isprimaryHeathCare already exists in facility_types.';
        END;
    END;
$$;