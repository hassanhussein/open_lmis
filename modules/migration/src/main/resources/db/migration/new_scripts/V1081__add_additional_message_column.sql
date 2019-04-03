DO $$
    BEGIN
        BEGIN
            ALTER TABLE interface_response_messages ADD COLUMN message text;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column message already exists in interface_response_messages.';
        END;
    END;
$$;