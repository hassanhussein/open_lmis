
ALTER TABLE requisition_status_changes
DROP COLUMN IF EXISTS rejectionlevel;

ALTER TABLE requisition_status_changes
    ADD COLUMN rejectionLevel character varying(200) default null;