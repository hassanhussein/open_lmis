
ALTER TABLE vaccine_report_coverage_line_items
    DROP COLUMN IF EXISTS withinfacilitymale;

ALTER TABLE vaccine_report_coverage_line_items
    DROP COLUMN IF EXISTS withinfacilityfemale;

ALTER TABLE vaccine_report_coverage_line_items
    ADD COLUMN withinfacilitymale integer;

ALTER TABLE vaccine_report_coverage_line_items
    ADD COLUMN withinfacilityfemale integer;