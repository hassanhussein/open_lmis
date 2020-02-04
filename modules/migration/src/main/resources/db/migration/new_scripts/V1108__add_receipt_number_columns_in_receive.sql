
ALTER TABLE receive_line_items
    DROP COLUMN IF EXISTS receiveNumber;
ALTER TABLE receive_line_items
    ADD COLUMN receiveNumber character varying(250)  default null;