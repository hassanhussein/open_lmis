DROP TABLE IF EXISTS facility_vaccine_transfers;

CREATE TABLE facility_vaccine_transfers
(
  id serial,
  facilityid integer,
  quantity integer,
  lineItemId INTEGER,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT facility_vaccine_transfers_pkey PRIMARY KEY (id),
    CONSTRAINT facility_vaccine_transfers_fkey FOREIGN KEY (lineItemId)
      REFERENCES vaccine_report_logistics_line_items(id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


ALTER TABLE vaccine_report_logistics_line_items
    DROP COLUMN IF EXISTS totalAdjustedQuantity;
    ALTER TABLE vaccine_report_logistics_line_items
    DROP COLUMN IF EXISTS quantityTransferredId;

ALTER TABLE vaccine_report_logistics_line_items
    ADD COLUMN totalAdjustedQuantity integer;

ALTER TABLE vaccine_report_logistics_line_items
  ADD COLUMN quantityTransferredId integer;
