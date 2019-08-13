ALTER TABLE patient_line_items
ADD COLUMN skipped BOOLEAN;

update patients set active=true where code='Number of Retreatment patients';



INSERT INTO program_patient_columns(name, programId, label, visible, dataType, displayorder, fixed) values
('monthOfTreatment',(select id from programs where code='TB&LEPROSY'), 'header.monthOfTreatment',true,'patient.reporting.dataType.numeric', 1, true),