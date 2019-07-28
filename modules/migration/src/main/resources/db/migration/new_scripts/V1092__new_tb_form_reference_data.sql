INSERT INTO programs (code, name, description, active, templateConfigured, regimenTemplateConfigured, budgetingApplies, usesDar, push)
  VALUES
  ('TB&LEPROSY', 'TB_Monthly', 'TB and Leprsoy monthly report', TRUE, FALSE, FALSE, FALSE, FALSE, FALSE);



INSERT INTO master_rnr_columns
(   name                           ,  position  ,                         label                                  , source         , sourceConfigurable         , formula                                                  , indicator                                           , used       , visible       , mandatory    , description)
VALUES
(  'nextMonthPatient'              ,         26 , 'No of Patients on Treatment next month'                       ,'C'             , false                      , ''                                                       , 'indicator.column.nextMonthPatient'                 , true       , true          , false        , 'description.column.nextMonthPatient'                    ),
(  'dosesPerMonth'                 ,         27 , 'Individual Monthly Requirement'                               ,'R'             , false                      , ''                                                       , 'indicator.column.monthly.requirement'              , true       , true          , false        , 'description.column.monthly.requirement'                 ),
(  'totalRequirement'              ,         28 , 'Total Requirement'                                            ,'C'             , false                      , 'formula.column.total.requirement'                       , 'indicator.column.total.requirement'                , true       , true          , true         , 'description.column.total.requirement'                   ),
(  'totalQuantityNeededByHF'       ,         29 , 'Total Quantity Needed by HF'                                  ,'C'             , false                      , 'formula.column.total.quantity.HF'                       , 'indicator.column.total.quantity.HF'                , true       , true          , false        , 'description.column.total.quantity.HF'                   ),
(  'quantityToIssue'               ,         30 , 'Quantity to Issue'                                            ,'C'             , false                      , 'formula.column.quantity.to.issue'                       , 'indicator.column.quantity.to.issue'                , true       , true          , false        , 'description.column.quantity.to.issue'                   ),
(  'remarksForTBDispensedQuantity' ,         31 , 'Remarks'                                                      ,'U'             , false                      , 'formula.column.tb.remarks'                              , 'indicator.column.tb.remarks'                       , true       , true          , false        , 'description.column.tb.remarks'                          );



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



Drop TABLE IF EXISTS program_patient_columns;

CREATE TABLE program_patient_columns (
    id SERIAL PRIMARY KEY,
    programId INTEGER NOT NULL REFERENCES programs(id),
    name varchar(100) NOT NULL,
    label varchar(100) NOT NULL,
    visible boolean NOT NULL,
    fixed boolean NOT NULL,
    dataType varchar(50) NOT NULL,
    displayorder integer NOT NULL DEFAULT 0,
    createdBy INTEGER,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modifiedBy INTEGER,
    modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX i_program_patient_name ON program_patient_columns(programId, name);



DELETE FROM program_patient_columns;

INSERT INTO program_patient_columns(name, programId, label, visible, dataType, displayorder, fixed) values
('monthOfTreatment',(select id from programs where code='TB&LEPROSY'), 'header.monthOfTreatment',true,'patient.reporting.dataType.numeric', 1, true),
('firstMonth',(select id from programs where code='TB&LEPROSY'), 'header.firstMonth',true,'patient.reporting.dataType.numeric', 2, false),
('secondMonth',(select id from programs where code='TB&LEPROSY'), 'header.secondMonth',true,'patient.reporting.dataType.numeric', 3, false),
('thirdMonth',(select id from programs where code='TB&LEPROSY'), 'header.thirdMonth',true,'patient.reporting.dataType.numeric', 4, false),
('fourthMonth',(select id from programs where code='TB&LEPROSY'), 'header.fourthMonth',true,'patient.reporting.dataType.numeric', 5, false),
('fifthMonth',(select id from programs where code='TB&LEPROSY'), 'header.fifthMonth',true,'patient.reporting.dataType.numeric', 6, false),
('sixthMonth',(select id from programs where code='TB&LEPROSY'), 'header.sixthMonth',true,'patient.reporting.dataType.numeric', 7, false),
('seventhMonth',(select id from programs where code='TB&LEPROSY'), 'header.seventhMonth',true,'patient.reporting.dataType.numeric', 8, false),
('eighthMonth',(select id from programs where code='TB&LEPROSY'), 'header.eighthMonth',true,'patient.reporting.dataType.numeric', 9, false),
('ninthMonth',(select id from programs where code='TB&LEPROSY'), 'header.ninthMonth',true,'patient.reporting.dataType.numeric', 10, false),
('tenthMonth',(select id from programs where code='TB&LEPROSY'), 'header.tenthMonth',true,'patient.reporting.dataType.numeric', 11, false),
('eleventhMonth',(select id from programs where code='TB&LEPROSY'), 'header.eleventhMonth',true,'patient.reporting.dataType.numeric', 12, false),
('twelfthMonth',(select id from programs where code='TB&LEPROSY'), 'header.twelfthMonth',true,'patient.reporting.dataType.numeric', 13, false);




DROP TABLE IF EXISTS patient_categories;
CREATE TABLE patient_categories (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    displayOrder INTEGER NOT NULL,
    createdBy INTEGER,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modifiedBy INTEGER,
    modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



DELETE FROM patient_categories;

INSERT INTO patient_categories (code, name, displayOrder) VALUES
('TB', 'TB', 1),
('LEPROSY', 'Leprosy', 2);




DROP TABLE IF EXISTS patients;
CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    programId INTEGER NOT NULl references programs(id),
    categoryId INTEGER NOT NULL references patient_categories(id),
    code VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    active   BOOLEAN,
    displayOrder INTEGER NOT NULL,
    createdBy INTEGER,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modifiedBy INTEGER,
    modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT patient_code_key UNIQUE (code)
);

CREATE UNIQUE INDEX i_patients_code_programId ON patients(code, programId);


DELETE FROM patients;

INSERT INTO patients (programid, categoryid, code, name, active, displayorder) VALUES
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='TB'), 'Number of adult patients(New)', 'Number of adult patients(New)', TRUE, 1),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='TB'), 'Number of children(New)', 'Number of children(New)', TRUE, 2),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='TB'), 'Number of Retreatment patients', 'Number of Retreatment patients', TRUE, 3),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='TB'), 'Number of adults on IPT', 'Number of adults on IPT', TRUE, 4),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='TB'), 'Number of Children on IPT', 'Number of Children on IPT', TRUE, 5),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='LEPROSY'), 'Number of adult on MB regimen', 'Number of adult on MB regimen', TRUE, 6),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='LEPROSY'), 'Number of adult on PB regimen', 'Number of adult on PB regimen', TRUE, 7),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='LEPROSY'), 'Number of children on MB regimen', 'Number of children on MB regimen', TRUE, 8),
((select id from programs where code='TB&LEPROSY'), (select id from patient_categories where code='LEPROSY'), 'Number of children on PB regimen', 'Number of children on PB regimen', TRUE, 9);





DROP TABLE IF EXISTS patient_line_items;
CREATE TABLE patient_line_items (
  id                         SERIAL PRIMARY KEY,
  rnrId                      INT         NOT NULL REFERENCES requisitions (id),
  code                VARCHAR(50) NOT NULL REFERENCES patients (code),
  name                       VARCHAR(50),
  patientCategory            VARCHAR(50),
  patientDisplayorder        INTEGER,
  patientCategoryDisplayOrder INTEGER,
  firstMonth                 INTEGER,
  secondMonth                INTEGER,
  thirdMonth                 INTEGER,
  fourthMonth                INTEGER,
  fifthMonth                 INTEGER,
  sixthMonth                 INTEGER,
  seventhMonth               INTEGER,
  eighthMonth                INTEGER,
  ninthMonth                 INTEGER,
  tenthMonth                 INTEGER,
  eleventhMonth              INTEGER,
  twelfthMonth               INTEGER,
  createdBy                  INTEGER,
  createdDate                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modifiedBy                 INTEGER,
  modifiedDate               TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX i_patient_line_items_rnrId_t ON patient_line_items (rnrId);


--ALTER TABLE requisition_line_items
--ADD COLUMN individualMonthlyRequirement INTEGER;


ALTER TABLE requisition_line_items
ADD COLUMN totalRequirement INTEGER;

ALTER TABLE requisition_line_items
ADD COLUMN totalQuantityNeededByHF INTEGER;

ALTER TABLE requisition_line_items
ADD COLUMN quantityToIssue INTEGER;

ALTER TABLE requisition_line_items
ADD COLUMN remarksForTBDispensedQuantity VARCHAR(255);

ALTER TABLE requisition_line_items
ADD COLUMN nextMonthPatient INTEGER;


ALTER TABLE products
ADD COLUMN patientCalculationFormula VARCHAR(255);



----Insert program_products
--insert into program_products(programId, productId, fullSupply, dosesPerMonth, currentPrice, active, displayOrder, productCategoryId) values
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010087AE'),'true',112,0.00,'true',455,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010086AE'),'true',112,0.00,'true',0,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010110AE'),'true',1,0,'true',9,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010113AE'),'true',1,0,'true',356,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010112AE'),'true',1,0,'true',356,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010310AE'),'true',1,0,'true',356,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010256AE'),'true',112,0,'true',356,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10020027AE'),'true',84,0,'true',356,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010255AE'),'true',112,0,'true',356,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010114AE'),'true',56,0,'true',356,(select id from product_categories where code='cgi')),
--((select id from programs where code='TB&LEPROSY'),(select id from products where code = '10010089AE'),'true',28,0,'true',356,(select id from product_categories where code='cgi'));
--
--
--
----Program prodcut mapping
--
----RHZE
--update  products set patientcalculationformula='ADULT_NEW_INTENSIVE_PHASE'
--where  code='10010087AE';
--
----RH 150/75
--update  products set patientcalculationformula='ADULT_NEW_CONTINUATION_PHASE'
--where   code='10010086AE';
--
--
----MB(Adult)
--update  products set patientcalculationformula='MB_ADULT'
--where  code='10010110AE';
--
----MB(Pedriatics)
--update  products set patientcalculationformula='MB_PEDIATRIC'
--where  code='10010113AE';
--
----PB(Adult)
--update  products set patientcalculationformula='PB_ADULT'
--where  code='10010112AE';
--
----PB(Pedriatics)
--update  products set patientcalculationformula='PB_PEDIATRIC'
--where  code='10010310AE';
--
--
----RHZ 75/50/150
--update  products set patientcalculationformula='CHILD_NEW_INTENSIVE_PHASE'
--where  code='10010256AE';
--
----Ethambutol 100mg
--update  products set patientcalculationformula='CHILD_NEW_INTENSIVE_PHASE'
--where  code='10020027AE';
--
--
----RH75/50
--update  products set patientcalculationformula='CHILD_NEW_CONTINUATION_PHASE'
--where  code='10010255AE';
--
----Isonizid 100mg
--update  products set patientcalculationformula='IPT_PEDIATRIC'
--where  code='10010114AE';
--
----Isonizid 300mg
--update  products set patientcalculationformula='IPT_ADULT'
--where code='10010089AE';
