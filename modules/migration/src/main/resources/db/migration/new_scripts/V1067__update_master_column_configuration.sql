--SEED PROGRAM

/*

DELETE from vaccine_program_logistics_columns where label = 'Adjusted Quantity';

INSERT INTO public.vaccine_program_logistics_columns(
            programid, mastercolumnid, label, displayorder, visible,
            createdby, createddate, modifiedby, modifieddate)
WITH Q as (
select * from vaccine_logistics_master_columns where name = 'totalAdjustedQuantity'
)
select (select id from programs where lower(code) = lower('VACCINE')  limit 1) programId,id mastercolumnid,label,displayOrder, true visible, 1 createdby, NOW() createddate, 1 modifiedby, NOW() modifieddate FROM Q;
 */

--Additional Column in vaccine_logistics line Items

ALTER TABLE vaccine_report_logistics_line_items
    DROP COLUMN IF EXISTS transferOutQuantity;

ALTER TABLE vaccine_report_logistics_line_items
    DROP COLUMN IF EXISTS transferInQuantity;

ALTER TABLE vaccine_report_logistics_line_items
    ADD COLUMN transferOutQuantity numeric(6,1);

ALTER TABLE vaccine_report_logistics_line_items
    ADD COLUMN transferInQuantity integer;
