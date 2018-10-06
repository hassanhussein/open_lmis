DELETE FROM vaccine_logistics_master_columns WHERE name ='totalAdjustedQuantity';

INSERT INTO vaccine_logistics_master_columns
(name, description, label,indicator, displayOrder, mandatory)
VALUES
('totalAdjustedQuantity', 'Adjusted Quantity', 'Adjusted Quantity', 'P', 17, false);

/*delete from vaccine_program_logistics_columns where label = 'Adjusted Quantity';

INSERT INTO public.vaccine_program_logistics_columns(
            programid, mastercolumnid, label, displayorder, visible,
            createdby, createddate, modifiedby, modifieddate)
    VALUES (82, 17, 'Adjusted Quantity', 17, true,1, NOW(), 1, NOW());*/



/*
Additional Column in vaccine_logistics line Items
*/
ALTER TABLE vaccine_report_logistics_line_items
    DROP COLUMN IF EXISTS transferOutQuantity;

ALTER TABLE vaccine_report_logistics_line_items
    DROP COLUMN IF EXISTS transferInQuantity;

ALTER TABLE vaccine_report_logistics_line_items
    ADD COLUMN transferOutQuantity numeric(6,1);

ALTER TABLE vaccine_report_logistics_line_items
    ADD COLUMN transferInQuantity integer;