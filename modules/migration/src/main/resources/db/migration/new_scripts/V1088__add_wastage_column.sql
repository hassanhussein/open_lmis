DO $$
    BEGIN
        BEGIN
            ALTER TABLE losses_adjustments_types ADD COLUMN wastage Boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column wastage already exists in losses_adjustments_types.';
        END;
    END;
$$;


update losses_adjustments_types set wastage = true where name in ('STOLEN','DAMAGED','LOST','EXPIRED')
