DO $$
    BEGIN
        BEGIN
            ALTER TABLE hfr_facilities ADD COLUMN districtCode CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column districtCode already exists in hfr_facilities.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE hfr_facilities ADD COLUMN councilCode CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column councilCode already exists in hfr_facilities.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE hfr_facilities ADD COLUMN facilityTypeGroupCode CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column facilityTypeGroupCode already exists in hfr_facilities.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE hfr_facilities ADD COLUMN ownershipCode CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column ownershipCode already exists in hfr_facilities.';
        END;
    END;
$$;



ALTER TABLE hfr_facilities DROP COLUMN  if exists IlIDNumber;
