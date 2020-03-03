
DO $$
    BEGIN
        BEGIN
            ALTER TABLE EQUIPMENTS ADD COLUMN CODE CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column msdCode already exists in EQUIPMENTS.';
        END;
    END;
$$;
DO $$
    BEGIN
        BEGIN
            ALTER TABLE EQUIPMENTS ADD COLUMN manufacturerId integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column manufacturerId already exists in EQUIPMENTS.';
        END;
    END;
$$;
DO $$
    BEGIN
        BEGIN
            ALTER TABLE MANUFACTURERS ADD COLUMN CODE CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column code already exists in MANUFACTURERS.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_energy_types ADD COLUMN CODE CHARACTER VARYING (250) DEFAULT NULL;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column code already exists in equipment_energy_types.';
        END;
    END;
$$;

ALTER TABLE equipment_service_contract_equipment_types DROP CONSTRAINT IF EXISTS equipment_service_contract_equipments_contractid_fkey;
 ALTER TABLE equipment_service_contract_equipment_types DROP COLUMN if exists equipmentTypeId;
DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_service_contract_equipment_types ADD COLUMN equipmentId INTEGER;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column code already exists in equipmentId.';
        END;
    END;
$$;


