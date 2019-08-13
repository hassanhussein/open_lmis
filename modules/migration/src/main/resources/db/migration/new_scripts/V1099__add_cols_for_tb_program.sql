ALTER TABLE patient_line_items
ADD COLUMN skipped BOOLEAN;

update patients set active=true where code='Number of Retreatment patients';