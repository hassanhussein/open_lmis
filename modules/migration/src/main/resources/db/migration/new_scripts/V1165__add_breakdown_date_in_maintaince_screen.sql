DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_maintenance_requests  ADD COLUMN breakDownDate  date;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column breakDownDate already exists in equipment_maintenance_requests.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_maintenance_requests  ADD COLUMN approved boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column approved already exists in equipment_maintenance_requests.';
        END;
    END;
$$;


DO $$
    BEGIN
        BEGIN
            ALTER TABLE equipment_inventories  ADD COLUMN dateofinstallation timestamp without time zone default now();
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column dateofinstallation already exists in equipment_inventories.';
        END;
    END;
$$;