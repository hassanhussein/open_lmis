
DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_types  ADD COLUMN categoryId  integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column categoryId already exists in equipment_types';
        END;
    END;
$$;
