DO $$
    BEGIN
        BEGIN
            ALTER TABLE supervisory_nodes ADD COLUMN skipApproval boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column skipApproval already exists in supervisory_nodes.';
        END;
    END;
$$;