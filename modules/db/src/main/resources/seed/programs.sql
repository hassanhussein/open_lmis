--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--

ALTER TABLE programs DISABLE TRIGGER ALL;
TRUNCATE programs RESTART IDENTITY CASCADE;
ALTER TABLE programs ENABLE TRIGGER ALL;

INSERT INTO programs (code, name, description, active, templateConfigured, regimenTemplateConfigured, budgetingApplies, usesDar, push)
  VALUES
  ('HIV', 'HIV', 'HIV', TRUE, FALSE, FALSE, FALSE, FALSE, FALSE),
  ('ESS_MEDS', 'ESSENTIAL MEDICINES', 'ESSENTIAL MEDICINES', TRUE, FALSE, TRUE, TRUE, FALSE, FALSE),
  ('TB', 'TB', 'TB', TRUE, FALSE, FALSE, FALSE, FALSE, FALSE),
    ('MALARIA', 'MALARIA', 'MALARIA', TRUE, FALSE, FALSE, TRUE, FALSE, FALSE),
    ('VACCINES', 'VACCINES', 'VACCINES', TRUE, FALSE, FALSE, FALSE, FALSE, TRUE),
    ('TB&LEPROSY', 'TB_Monthly', 'TB and Leprsoy monthly report', TRUE, FALSE, FALSE, FALSE, FALSE, FALSE);


ALTER TABLE program_rnr_columns DISABLE TRIGGER ALL;
TRUNCATE program_rnr_columns RESTART IDENTITY CASCADE;
ALTER TABLE program_rnr_columns ENABLE TRIGGER ALL;
INSERT INTO program_rnr_columns
(masterColumnId, programId, visible, source, position, label, rnrOptionId) VALUES
((select id from master_rnr_columns where name='skipped'), (select id from programs where code = 'TB&LEPROSY'),  true, 'U', 1,  'Skip', null),
((select id from master_rnr_columns where name='productCode'), (select id from programs where code = 'TB&LEPROSY'),  true, 'R', 2,  'MSD Code', null),
((select id from master_rnr_columns where name='product'), (select id from programs where code = 'TB&LEPROSY'),  true, 'R', 3,  'Medicine Name', null),
((select id from master_rnr_columns where name='dispensingUnit'), (select id from programs where code = 'TB&LEPROSY'),  true, 'R', 4,  'Unit of Measure (U)', null),
((select id from master_rnr_columns where name='stockInHand'), (select id from programs where code = 'TB&LEPROSY'),  true, 'U', 5,  'Stock on Hand (A)', null),
((select id from master_rnr_columns where name='lossesAndAdjustments'), (select id from programs where code = 'TB&LEPROSY'),  true, 'U', 6,  'Qnty damaged & Expired (B)', null),
((select id from master_rnr_columns where name='nextMonthPatient'), (select id from programs where code = 'TB&LEPROSY'),  true, 'C', 7,  'No of Patients on Treatment next month', null),
((select id from master_rnr_columns where name='dosesPerMonth'), (select id from programs where code = 'TB&LEPROSY'),  true, 'R', 8,  'Individual Monthly Requirement', null),
((select id from master_rnr_columns where name='totalRequirement') , (select id from programs where code = 'TB&LEPROSY'),  true, 'C', 9,  'Total Requirement (E) E = C * D', null),
((select id from master_rnr_columns where name='totalQuantityNeededByHF'), (select id from programs where code = 'TB&LEPROSY'),  true, 'C', 10,  'Total Quantity Needed by HF (F) F ß= E * 2', null),
((select id from master_rnr_columns where name='quantityToIssue'), (select id from programs where code = 'TB&LEPROSY'),  true, 'C', 11, 'Quantity to Issue (G) G = F - A', null),
((select id from master_rnr_columns where name='total'), (select id from programs where code = 'TB&LEPROSY'),  true, 'C', 12, 'Quantity to issue  (converted to unit of measure) G/U', null),
((select id from master_rnr_columns where name='quantityReceived'), (select id from programs where code = 'TB&LEPROSY'),  true, 'U', 13, 'Quantity Issued', null),
((select id from master_rnr_columns where name='remarksForTBDispensedQuantity'), (select id from programs where code = 'TB&LEPROSY'),  true, 'U', 14,  'Remarks', null);



update programs set templateConfigured = true where id in ((select id from programs where code = 'TB&LEPROSY'));

