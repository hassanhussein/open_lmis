ALTER TABLE interface_dataset
    DROP COLUMN IF EXISTS elmiscode;

ALTER TABLE interface_dataset
    ADD COLUMN elmiscode character varying(100);

--create index msd_stock_status_facility_product on msd_stock_statuses (facilitycode, productcode);