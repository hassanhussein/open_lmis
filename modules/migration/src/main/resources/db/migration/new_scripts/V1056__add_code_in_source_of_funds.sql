
DO $$
    BEGIN
        BEGIN
            ALTER TABLE source_of_funds ADD COLUMN code character varying(100);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column code already exists in source_of_funds.';
        END;
    END;
$$;

ALTER TABLE source_of_funds DROP CONSTRAINT IF EXISTS source_of_fund_code;
ALTER TABLE source_of_funds ADD CONSTRAINT source_of_fund_code UNIQUE (code);