DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipments ADD COLUMN equipmentCategoryId CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column equipmentCategoryId already exists in equipments.';
        END;
    END;
$$;
