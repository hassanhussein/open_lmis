

--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--

ALTER TABLE facility_approved_products DISABLE TRIGGER ALL;
ALTER TABLE program_products DISABLE TRIGGER ALL;
ALTER TABLE products DISABLE TRIGGER ALL;
ALTER TABLE product_groups DISABLE TRIGGER ALL;
ALTER TABLE dosage_units DISABLE TRIGGER ALL;
ALTER TABLE product_categories DISABLE TRIGGER ALL;
ALTER TABLE product_forms DISABLE TRIGGER ALL;

delete from facility_approved_products;
delete from program_products;
delete from products;
delete from product_groups;
delete from dosage_units;
delete from product_categories;
delete from product_forms;


ALTER TABLE facility_approved_products ENABLE TRIGGER ALL;
ALTER TABLE program_products ENABLE TRIGGER ALL;
ALTER TABLE products ENABLE TRIGGER ALL;
ALTER TABLE product_groups ENABLE TRIGGER ALL;
ALTER TABLE dosage_units ENABLE TRIGGER ALL;
ALTER TABLE product_categories ENABLE TRIGGER ALL;
ALTER TABLE product_forms ENABLE TRIGGER ALL;


INSERT INTO product_forms(code, displayOrder) VALUES
('Tablet',1),
('Capsule',2),
('Bottle',3),
('Vial',4),
('Ampule',5),
('Drops',6),
('Powder',7),
('Each',8),
('Injectable',9),
('Tube',10),
('Solution',11),
('Inhaler',12),
('Patch',13),
('Implant',14),
('Sachet',15),
('Device',16),
('Other',17);



INSERT INTO dosage_units
(code, displayOrder) VALUES
('mg',1),
('ml',2),
('each',3),
('cc',4),
('gm',5),
('mcg',6),
('IU',7);


--insert product groups
insert into product_groups
(code, name ) values
    ('ane','Anaesthetics and Analgesics'),
    ('anw','Anti warts'),
    ('anb','Antibiotics'),
    ('anf','Antifungals'),
    ('anh','Antihelminthics'),
    ('anm','Antimalarials'),
    ('arv','Antiretrovirals'),
    ('ans','Antiseptics/Disinfectants'),
    ('anp','Antispasmodics'),
    ('bro','Bronchodilators'),
    ('cns','Central Nervous system drugs'),
    ('con','Contraceptives'),
    ('git','GIT drugs'),
    ('ivf','Intravenous Fluids'),
    ('lab','Laboratory utensils/reagents'),
    ('mgt','Management tools'),
    ('msu','Medical supplies and utensils'),
    ('opp','Ophthalmic preparation'),
    ('oxy','Uterotonic'),
    ('pma','PMTCT Option A'),
    ('pmb','PMTCT Option B+'),
    ('sup','Supplements'),
    ('atb','Antitb'),
    ('anl','Antileprosy'),
    ('ana','antiallergy'),
    ('ute','uterotonics');


insert into product_categories
(code, name , displayOrder) values
   ('arv','Antiretrovirals',1),
    ('anb','Antibiotics',4),
    ('anh','Antihelminthics',5),
    ('anf','Antifungals',6),
    ('anm','Antimalarials',7),
    ('bro','Bronchodilators',8),
    ('cns','Central Nervous system drugs',9),
    ('con','Contraceptives',10),
    ('sup','Supplements',11),
    ('git','GIT drugs',12),
    ('opp','Ophthalmic preparation',13),
    ('ans','Antiseptics/Disinfectants',14),
    ('anp','Antispasmodics',15),
    ('anw','Anti warts',16),
    ('oxy','Oxytocics',17),
    ('pma','PMTCT Option A',18),
    ('pmb','PMTCT Option B+',19),
    ('ivf','Intravenous Fluids',20),
    ('msu','Medical supplies and utensils',21),
    ('lab','Laboratory utensils/reagents',22),
    ('mgt','Management tools',23),
    ('anl','Antileprosy',24),
    ('ute','Uterotonics',26),
    ('oid','Opportunistic Infections Drugs',27),
    ('chem','Chemistry',28),
    ('oth','Other Methods',29),
    ('ser','Serology',30),
    ('bac','Bacteriology (Stains and Bacterial ID agents)',31),
    ('acc','Accessories and other Supplies',32),
    ('hae','Haematology',33),
    ('cdf','CD-4 Testing Facs Count',34),
    ('axy','AXYSM',35),
    ('vil','Viral Load',36),
    ('pcr','PCR Test',37),
    ('bga','Blood and glucose analyser',38),
    ('chu','Reagents Strip for Urine - Chemistry',39),
    ('his','Histopathology',40),
    ('bfi','Bacterial and Fungal Isolation and Identification',41),
    ('bsi','Bacterial Stains and Identification Agent',42),
    ('pry','Parasitology',43),
    ('cgi','Consumables and General Items',44),
    ('hep','Haematology Pentra 8',46),
    ('fas','Facs Callibur',47),
    ('btf','Blood Transfusion',48),
    ('ant','Anti - Inflamatory',49),
    ('antang','Anti-Angina Medicines',31),
    ('gen','General Purposes, Laboratory and Disinfectants Items',51),
    ('hospef','Hospital Equipment And Furniture ',52),
    ('hosplub','Hospital Linen, Uniforms & Beddings Materials ',53),
    ('hhw','Hospital Hollow Wares ',54),
    ('patmgmnt','Patient Management Devices And Other Tools ',55),
    ('psychoa','Psychotherapeutic Agents',56),
    ('reud','Reusable Devices (Hospital Instruments) ',57),
    ('atb','Anti-TB',25),
    ('antac','Anti - Acid Medicines',58),
    ('antdb','Anti - Diabetic Medicines',59),
    ('anhc','Anti - Hypertensive and Cardiac Glycoside',60),
    ('antp','Antipruritics',61),
    ('cat','Catheters &Tubes',62),
    ('DiureticMed','Diuretic Medicines',63),
    ('MicroSerol','Microbiology and Serology Items',64),
    ('Patmgmntdevice','Patient Management Devices And Other Tools',65),
    ('psychothagent','Psychotherapeutic Agents',66),
    ('steroid','Steroidal Anti-Inflammatory Medicines',67),
    ('surgicalInst','Surgical Instruments & Medical Kits/Sets',68),
    ('syrin','Syringes, Needles, Cannulas &  Administration Sets',69),
    ('vit','Vitamins & Minerals',70),
    ('wasteCollect','Waste Collection Bags',71),
    ('dress','Dressing Material ',72),
    ('ana','Anti-allergies and Medicines used Anaphylaxis and Shock',3),
    ('cough','Cough Medicine',72),
    ('NSAIDS','Anti-pyretics NSAIDS',73),
    ('anschisto','Anti-Schistosomal',74),
    ('screenm','Screen Master',75),
    ('anlg','Analgesics',0),
    ('antidote','Antidote',76),
    ('hemamicro60','Hematology Micro 60',78),
    ('amoe','Amoebicides',1),
    ('lane','Local Anesthesia',2),
    ('gane','General Anesthesia',3),
    ('asth','Anti-Asthmatic and Cough Medicine',6),
    ('Anti-Co','Anti-Coagulant & Antagonists',79),
    ('Anti-Ep','Anti-Epileptics/Anticonvasants',80),
    ('Anti-Ha','Anti-Haemorrhoids',81),
    ('Anti-Py','Anti-Pyretics And Nsaids',82),
    ('Gloves','Gloves & Other Protective Gears',83),
    ('Ironde','Iron Deficiency Anaemias Medicines',84),
    ('Liq','Liquid, Correcting Electrolytes and Acid/Base Disturbances',85),
    ('Med-Diar','Medicines used in Diarrhoea',86),
    ('Muscle-Relax','Muscle Relaxants , Cholineserase Inhibitors and Anticholinergic',87),
    ('Narcoti','Narcotic & Antagonists',88),
    ('Ophtham','Ophthamological Preparations',89),
    ('SeraIm','Sera And Immunoglobulins',90),
    ('X-RayC','X-Ray Chemicals And Reagents',91),
    ('X-RayF','X-Ray Films',92),
    ('ane','Anaesthetics',1),
    ('hmind','Hematology Reagents for Mindray Machines BC 3200',99),
    ('hsys','Haematology Reagents for Sysmex (KX21, KX 1000i/PoCH)',110),
    ('gds','GIT drugs and Suppliments',122),
    ('ecfvm','Electrolystes corection  Fluid, Vaccines and Immunoglobins',123),
    ('adv','Antiretriviral Drugs -ARVs',124),
    ('aaam','Anti-Allergies and anaphylaxis mediecine',114),
    ('ast','Asthma',115),
    ('anhs','Antihelminthes',116),
    ('anfs','Anti-Fungals',117),
    ('antd','Anti-Diabetics',118),
    ('ahcg','Anti - Hypertensive and Cardiac Glycoside.',119),
    ('amm','Anti-Malarial medicine',120),
    ('cnsm','Central Nervous System Medicines',121),
    ('agmr','Anesthetics General and Muscle relaxants',112),
    ('anta','Antibiotics and Amoebicides',113),
    ('laa','Local Anaesthetics and analgesics',111),
    ('nar','Narcotics',126),
    ('fpsmp','Family Planning and Safe Motherhood product',127),
    ('ansd','Antiseptics/Disinfectants.',128),
    ('Ophthamp','Ophthalmic Preparation.',129),
    ('msus','Medical supplies and utensils.',130),
    ('nmv','Nutritions (Minerals and Vitamins)',131),
    ('sergy','Serology.',132),
    ('hbtr','Hematology and Blood transfusion',133),
    ('chemy','Chemistry.',134),
    ('icd','Immunophenotyping/CD4',135),
    ('cosu','Consumables',136),
    ('lmt','LMIS and Management Tools',137),
    ('tbli','TB&LEPROSY ITEMS',138),
    ('ctfc','CD-4 Testing Facs Count.',201),
    ('vrl','Viral Load.',202),
    ('htlgy','Haematology.',203),
    ('htlgym','Haematology Micro 60.',204),
    ('htlgyrfs','Haematology Reagents for Sysmex (KX21, KX 1000i/PoCH).',205),
    ('htlgyrfmm','Haematology Reagents for Mindray Machines BC 3200.',206),
    ('bctrg','Bacteriology (Stains and Bacterial ID agents).',207),
    ('lur','Laboratory utensils/reagents.',208),
    ('rsfuc','Reagents Strip for urine - Chemistry.',209),
    ('cgs','Consumables and General items.',211),
    ('scc','Specimen Collection/Consumables',45),
    ('sccnew','Specimen Collection/Consumables.',210),
    ('hmp','Haematology Pentra 80.',212),
    ('dmn','Dawa za Magonjwa Nyemelezi',125);






--Insert products
insert into products
(code, alternateItemCode, manufacturer, manufacturerCode, manufacturerBarcode, mohBarcode, gtin, type, primaryName, fullName, genericName, alternateName, description, strength, formId, dosageUnitId, dispensingUnit, dosesPerDispensingUnit, packSize, alternatePackSize, storeRefrigerated, storeRoomTemperature, hazardous, flammable, controlledSubstance, lightSensitive, approvedByWho, contraceptiveCyp, packLength, packWidth, packHeight, packWeight, packsPerCarton, cartonLength, cartonWidth, cartonHeight, cartonsPerPallet, expectedShelfLife, specialStorageInstructions, specialTransportInstructions, active, fullSupply, tracer, packRoundingThreshold, roundToZero, archived, productgroupid) values
('10010129AC','10010129','1',1,1,1,1,'Zinc Sulphate','Zinc Sulphate','Zinc Sulphate','Zinc Sulphate','Zinc Sulphate','Zinc Sulphate','20',(select id from product_forms where code='Tablet'),(select id from dosage_units where code='mg' ),'100 Tablets',1,100,1,'false','false','false','false','false','true','true',0.0000,0.0000,0.0000,0.0000,0.0000,0,0.0000,0.0000,0.0000,0,0,0,'0','true','true','false',1,'true','true',NULL),
('10010031MD','10010031MD',NULL,NULL,NULL,NULL,NULL,NULL,'Griseofulvin',NULL,NULL,NULL,NULL,'500',(select id from product_forms where code='Tablet'),(select id from dosage_units where code='mg' ),'1000 Tabs',1,1000,1000,'false','false','false','false','false','false','true',1.0000,1.0000,1.0000,1.0000,1.0000,1,1.0000,1.0000,1.0000,1,1,1,'1','true','true','false',1,'true','false',NULL),
('10010222SC',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'LOSARTAN','LOSARTAN 50MG TABLETS',NULL,NULL,'LOSARTAN 50MG TABLETS','50',(select id from product_forms where code='Tablet'),(select id from dosage_units where code='mg' ),'28 Tablets',1,28,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'true','true','false',1,'true','true',NULL),
('10010190SP',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'LORATIDINE','LORATIDINE 10MG TABLETS',NULL,NULL,'LORATIDINE 10MG TABLETS','10',(select id from product_forms where code='Tablet'),(select id from dosage_units where code='mg' ),'30 Tablets',1,30,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'true','true','false',1,'true','true',NULL),
('10010190MD',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Loratidine Tablet',NULL,NULL,NULL,NULL,'10',(select id from product_forms where code='Tablet'),(select id from dosage_units where code='mg' ),'30 Tablets',1,30,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'true','true','false',1,'true','true',NULL);


--Insert program_products
insert into program_products(programId, productId, fullSupply, dosesPerMonth, currentPrice, active, displayOrder, productCategoryId) values
((select id from programs where code='ilshosp'),(select id from products where code = '10010129AC'),'true',1,0.00,'true',455,(select id from product_categories where code='cgi')),
((select id from programs where code='ilshosp'),(select id from products where code = '10010031MD'),'true',1,0.00,'true',0,(select id from product_categories where code='cgi')),
((select id from programs where code='ilshosp'),(select id from products where code = '10010222SC'),'true',1,6300.00,'true',560,(select id from product_categories where code='cgi')),
((select id from programs where code='ilshosp'),(select id from products where code = '10010190SP'),'true',1,2600.00,'true',9,(select id from product_categories where code='cgi')),
((select id from programs where code='ilshosp'),(select id from products where code = '10010190MD'),'true',1,133300.00,'true',356,(select id from product_categories where code='cgi'));





--Insert facility_approved_products
insert into facility_approved_products(facilityTypeId, programProductId, maxMonthsOfStock, modifieddate) values
((select id from facility_types where code='disp'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010129AC')), 3,'11/11/2012'),
((select id from facility_types where code='disp'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010031MD')), 3,'11/11/2012'),
((select id from facility_types where code='disp'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010222SC')), 3,'11/11/2012'),
((select id from facility_types where code='disp'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010190SP')), 3,'11/11/2012'),
((select id from facility_types where code='heac'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010129AC')), 3,'11/11/2012'),
((select id from facility_types where code='heac'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010031MD')), 3,'11/11/2012'),
((select id from facility_types where code='heac'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010222SC')), 3,'11/11/2012'),
((select id from facility_types where code='heac'), (select id from program_products where programId=((select id from programs where code='ilshosp'))
and productId=(select id from products where  code='10010190SP')), 3,'11/11/2012');