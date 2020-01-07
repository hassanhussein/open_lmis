
ALTER TABLE inspections
    DROP COLUMN IF EXISTS isShippedProvided;
ALTER TABLE inspections
    ADD COLUMN isShippedProvided boolean default true;
ALTER TABLE inspections
    DROP COLUMN IF EXISTS isShipped;
ALTER TABLE inspections
    ADD COLUMN isShipped boolean default true;

ALTER TABLE inspections
    DROP COLUMN IF EXISTS shippedComment;
ALTER TABLE inspections
    ADD COLUMN shippedComment character varying(250);

ALTER TABLE inspections
    DROP COLUMN IF EXISTS shippedProvidedComment;
ALTER TABLE inspections
    ADD COLUMN shippedProvidedComment character varying(250);

ALTER TABLE inspections
    DROP COLUMN IF EXISTS conditionOfBox;
ALTER TABLE inspections
    ADD COLUMN conditionOfBox character varying(250);

ALTER TABLE inspections
    DROP COLUMN IF EXISTS labelAttachedComment;
ALTER TABLE inspections
    ADD COLUMN labelAttachedComment character varying(250);