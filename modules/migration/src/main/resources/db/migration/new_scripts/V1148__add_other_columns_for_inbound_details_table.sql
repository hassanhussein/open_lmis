DO $$
    BEGIN
        BEGIN
            ALTER TABLE in_bound_details ADD COLUMN status character varying(20);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column status already exists in in_bound_details.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE in_bound_details ADD COLUMN trackingNumber character varying(20);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column trackingNumber already exists in in_bound_details.';
        END;
    END;
$$;

