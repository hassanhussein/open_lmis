    ALTER TABLE vaccine_distribution_line_items
    DROP COLUMN IF EXISTS gap;

    ALTER TABLE vaccine_distributions
     DROP COLUMN IF EXISTS pickListId;

    ALTER TABLE vaccine_distributions
     ADD COLUMN pickListId bigint default null;

    ALTER TABLE vaccine_distribution_line_items
    ADD COLUMN gap character varying(200) default null;
