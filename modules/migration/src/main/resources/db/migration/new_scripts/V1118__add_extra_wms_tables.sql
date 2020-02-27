ALTER TABLE inspections
    DROP COLUMN IF EXISTS varNumber;
ALTER TABLE inspections
    ADD COLUMN varNumber CHARACTER VARYING (250);

    ALTER TABLE inspections
    DROP COLUMN IF EXISTS invoiceNumber;
ALTER TABLE inspections
    ADD COLUMN invoiceNumber CHARACTER VARYING (250);

    ALTER TABLE inspection_lots
    DROP COLUMN IF EXISTS boxNumber;
ALTER TABLE inspection_lots
    ADD COLUMN boxNumber CHARACTER VARYING (250);

        ALTER TABLE receives
    DROP COLUMN IF EXISTS invoiceNumber;
ALTER TABLE receives
    ADD COLUMN invoiceNumber CHARACTER VARYING (250);

      ALTER TABLE asns
        DROP COLUMN IF EXISTS invoiceNumber;
    ALTER TABLE asns
        ADD COLUMN invoiceNumber CHARACTER VARYING (250);