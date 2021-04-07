INSERT INTO patient_categories (code, name, displayOrder) VALUES
('MDR-REGIMEN-4', '6Bdq-PAS-Lzd -Cfz-Cs	AND 12 PAS - Cfz - Cs', 4),
('MDR-REGIMEN-5', '6Lfx-Bdq-E-Cfz-Z	AND 12Lfx-Cfz-E', 5),
('MDR-REGIMEN-6', '6Lfx-Bdq-Dlm-Cfz-E AND 12Lfx-Cfz-E', 6),
('MDR-REGIMEN-7', '6Bdq-Lzd-Cfz-Cs–Dlm	AND 14Lzd-Cfz-Cs', 7),
('MDR-REGIMEN-9', '6Bdq-PAS-Cfz-Cs–Dlm AND 14PAS-Cfz-Cs', 9),
('MDR-REGIMEN-10', '6Lfx-Lzd-Cs–Pto-Z AND 14Lfx-Lzd–Cs-Z', 10),
('MDR-REGIMEN-11', '6Lfx-Lzd-Cs–Pto-Z AND 14Lfx-Lzd–Cs-Z', 11),
('MDR-REGIMEN-12', '20Lfx-Cs-Cfz-E-PAS', 12),
('MDR-REGIMEN-13', '6Bdq–Lzd–Lfx–Cfz–Cs–Z AND 3-5Lfx–Cfz–Cs–Z', 13),
('MDR-REGIMEN-14', '6Bdq–Dlm–Lfx–Cfz–Cs–Z AND 3-5Lfx–Cfz–Cs–Z', 14),
('MDR-REGIMEN-15', '6Lfx/Mfx-Lzd-Cs-Cfz-Dlm AND 3Lfx/Mfx-Lzd-Cs-Cfz', 15),
('MDR-REGIMEN-16', '6Lfx/Mfx-Lzd-Cs-Cfz-Dlm AND 3Lfx/Mfx-Lzd-Cs-Cfz', 16),
('MDR-REGIMEN-17', '6Lzd-Cs–Dlm-Cfz AND 3Lzd–Cs-Cfz', 17),
('MDR-REGIMEN-18', '6Lzd-Cs–Dlm-Cfz AND 3Lzd–Cs-Cfz', 18),
('MDR-REGIMEN-19', '9Lfx/Mfx-Lzd-Cs-Cfz', 19),
('MDR-REGIMEN-20', '9Lzd-Cs–PAS-Cfz', 20);



INSERT INTO patients (programid, categoryid, code, name, active, displayorder) VALUES
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-4'), 'MDR-REGIMEN-4', 'Number of patients', TRUE, 4),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-5'), 'MDR-REGIMEN-5', 'Number of patients', TRUE, 5),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-6'), 'MDR-REGIMEN-6', 'Number of patients', TRUE, 6),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-7'), 'MDR-REGIMEN-7', 'Number of patients', TRUE, 7),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-9'), 'MDR-REGIMEN-9', 'Number of patients', TRUE, 9),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-10'), 'MDR-REGIMEN-10', 'Number of patients', TRUE, 10),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-11'), 'MDR-REGIMEN-11', 'Number of patients', TRUE, 11),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-12'), 'MDR-REGIMEN-12', 'Number of patients', TRUE, 12),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-13'), 'MDR-REGIMEN-13', 'Number of patients', TRUE, 13),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-14'), 'MDR-REGIMEN-14', 'Number of patients', TRUE, 14),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-15'), 'MDR-REGIMEN-15', 'Number of patients', TRUE, 15),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-16'), 'MDR-REGIMEN-16', 'Number of patients', TRUE, 16),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-17'), 'MDR-REGIMEN-17', 'Number of patients', TRUE, 17),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-18'), 'MDR-REGIMEN-18', 'Number of patients', TRUE, 18),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-19'), 'MDR-REGIMEN-19', 'Number of patients', TRUE, 19),
((select id from programs where code='TB-MDR'), (select id from patient_categories where code='MDR-REGIMEN-20'), 'MDR-REGIMEN-20', 'Number of patients', TRUE, 20);

ALTER TABLE patient_categories ADD display boolean;

ALTER TABLE patient_categories ALTER COLUMN display SET DEFAULT false;

update patient_categories set display=true where code in ('MDR-REGIMEN-1','MDR-REGIMEN-2','MDR-REGIMEN-3');


ALTER TABLE patient_line_items ADD COLUMN thirteenthMonth INTEGER;
ALTER TABLE patient_line_items ADD COLUMN fourteenthMonth INTEGER;
ALTER TABLE patient_line_items ADD COLUMN fifteenthMonth INTEGER;
ALTER TABLE patient_line_items ADD COLUMN sixteenthMonth INTEGER;
ALTER TABLE patient_line_items ADD COLUMN seventeenthMonth INTEGER;
ALTER TABLE patient_line_items ADD COLUMN eighteenthMonth INTEGER;
ALTER TABLE patient_line_items ADD COLUMN nineteenthMonth INTEGER;
ALTER TABLE patient_line_items ADD COLUMN twentiethMonth INTEGER;

