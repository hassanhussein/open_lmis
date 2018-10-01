
ALTER TABLE vaccine_report_coverage_line_items
    DROP COLUMN IF EXISTS regularOutReachMale;

ALTER TABLE vaccine_report_coverage_line_items
    DROP COLUMN IF EXISTS regularOutReachFeMale;

ALTER TABLE vaccine_report_coverage_line_items
    ADD COLUMN regularOutReachMale integer;

ALTER TABLE vaccine_report_coverage_line_items
    ADD COLUMN regularOutReachFeMale integer;