--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--

INSERT INTO referencedata.programs (id, code, name, description, active, shownonfullsupplytab, periodsskippable, enabledatephysicalstockcountcompleted)
  VALUES
    ('19ef7927-2597-4f75-b594-a0807d71f14a','ilshosp','Redesigned ILS','ILS Hospital','true', 'true','false', 'false');
--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--
INSERT INTO referencedata.geographic_levels
(id, code, name, levelNumber) VALUES
(uuid_generate_v4(), 'country', 'Country', 1),
(uuid_generate_v4(), 'state', 'State', 2),
(uuid_generate_v4(), 'province', 'Province', 3),
(uuid_generate_v4(), 'district', 'District', 4);

INSERT INTO referencedata.geographic_zones
(id, code, name, levelId, parentId) values
(uuid_generate_v4(), 'Root', 'Root', (SELECT id from referencedata.geographic_levels WHERE code = 'country'), NULL);

INSERT INTO referencedata.geographic_zones
(id, code, name, levelId, parentId) values
(uuid_generate_v4(), 'Mozambique', 'Mozambique', (SELECT id from referencedata.geographic_levels WHERE code = 'country'), NULL);


INSERT INTO referencedata.geographic_zones
(id, code, name, levelId, parentId) values
(uuid_generate_v4(), 'Arusha', 'Arusha',(SELECT id from referencedata.geographic_levels WHERE code = 'state'), (SELECT id from referencedata.geographic_zones WHERE code = 'Root'));


INSERT INTO referencedata.geographic_zones
(id, code, name, levelId, parentId) values
(uuid_generate_v4(), 'Dodoma', 'Dodoma',(SELECT id from referencedata.geographic_levels WHERE code = 'province'), (SELECT id from referencedata.geographic_zones WHERE code = 'Arusha'));

INSERT INTO referencedata.geographic_zones
(id, code, name, levelId, parentId) values
(uuid_generate_v4(), 'Ngorongoro', 'Ngorongoro', (SELECT id from referencedata.geographic_levels WHERE code = 'district'), (SELECT id from referencedata.geographic_zones WHERE code = 'Dodoma'));






INSERT INTO referencedata.facility_operators
(id, code,      description,      displayOrder) VALUES
(uuid_generate_v4(), 'MoH',     'MoH',     1),
(uuid_generate_v4(), 'NGO',     'NGO',     2),
(uuid_generate_v4(), 'FBO',     'FBO',     3),
(uuid_generate_v4(), 'Private', 'Private', 4);



INSERT INTO referencedata.facility_types (id, code, name, description, displayOrder, active) VALUES
    (uuid_generate_v4(), 'disp','Dispensary','Dispensary',13,'true'),
    (uuid_generate_v4(), 'heac','Health Centre','Health Centre',14,'true'),
    (uuid_generate_v4(), 'ddho','District Designated Hospital','District Designated Hospital',10,'true');

--Insert Facilities Section
INSERT INTO referencedata.facilities (id, code, name, description, geographiczoneid,
  typeid, operatedbyid,
  active, golivedate, godowndate, comment, enabled)
            VALUES
    ('47cb7b3f-caf0-4164-bf09-46ed5192d3e2', 'MZ520495','Kibirizi','Dispensary',(select id from referencedata.geographic_zones where code ='Ngorongoro'),
    (select id from referencedata.facility_types where code ='disp'),(select id from referencedata.facility_operators where code='MoH'),
    'true','2013-01-01 00:00:00',NULL,NULL,'true'),
    ('14617116-12d5-4617-aad0-004c8a2cb83e', 'MZ510054','Katoro Bukoba DC','Health Centre',(select id from referencedata.geographic_zones where code ='Ngorongoro'),
    (select id from referencedata.facility_types where code ='heac'),(select id from referencedata.facility_operators where code='MoH'),
    'true','2013-01-01 00:00:00',NULL,NULL,'true');




--Insert Programs Supported

INSERT INTO referencedata.supported_programs (facilityId, programId, startDate, active) VALUES
  ((SELECT
      id
    from referencedata.facilities
    WHERE code = 'MZ520495'),
     (SELECT
      id
    from referencedata.programs
    WHERE code = 'ilshosp'), '11/11/12', TRUE);

  INSERT INTO referencedata.supported_programs (facilityId, programId, startDate, active) VALUES
  ((SELECT
      id
    from referencedata.facilities
    WHERE code = 'MZ510054'),
     (SELECT
      id
    from referencedata.programs
    WHERE code = 'ilshosp'), '11/11/12', TRUE);


--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--
insert into referencedata.orderable_display_categories
(id, code, displayname , displayOrder) values
   (uuid_generate_v4(),'arv','Antiretrovirals',1),
    (uuid_generate_v4(),'anb','Antibiotics',4),
    (uuid_generate_v4(),'anh','Antihelminthics',5),
    (uuid_generate_v4(),'anf','Antifungals',6),
    (uuid_generate_v4(),'anm','Antimalarials',7),
    (uuid_generate_v4(),'bro','Bronchodilators',8),
    (uuid_generate_v4(),'cns','Central Nervous system drugs',9),
    (uuid_generate_v4(),'con','Contraceptives',10),
    (uuid_generate_v4(),'sup','Supplements',11),
    (uuid_generate_v4(),'git','GIT drugs',12),
    (uuid_generate_v4(),'opp','Ophthalmic preparation',13),
    (uuid_generate_v4(),'ans','Antiseptics/Disinfectants',14),
    (uuid_generate_v4(),'anp','Antispasmodics',15),
    (uuid_generate_v4(),'anw','Anti warts',16),
    (uuid_generate_v4(),'oxy','Oxytocics',17),
    (uuid_generate_v4(),'pma','PMTCT Option A',18),
    (uuid_generate_v4(),'pmb','PMTCT Option B+',19),
    (uuid_generate_v4(),'ivf','Intravenous Fluids',20),
    (uuid_generate_v4(),'msu','Medical supplies and utensils',21),
    (uuid_generate_v4(),'lab','Laboratory utensils/reagents',22),
    (uuid_generate_v4(),'mgt','Management tools',23),
    (uuid_generate_v4(),'anl','Antileprosy',24),
    (uuid_generate_v4(),'ute','Uterotonics',26),
    (uuid_generate_v4(),'oid','Opportunistic Infections Drugs',27),
    (uuid_generate_v4(),'chem','Chemistry',28),
    (uuid_generate_v4(),'oth','Other Methods',29),
    (uuid_generate_v4(),'ser','Serology',30),
    (uuid_generate_v4(),'bac','Bacteriology (uuid_generate_v4(),Stains and Bacterial ID agents)',31),
    (uuid_generate_v4(),'acc','Accessories and other Supplies',32),
    (uuid_generate_v4(),'hae','Haematology',33),
    (uuid_generate_v4(),'cdf','CD-4 Testing Facs Count',34),
    (uuid_generate_v4(),'axy','AXYSM',35),
    (uuid_generate_v4(),'vil','Viral Load',36),
    (uuid_generate_v4(),'pcr','PCR Test',37),
    (uuid_generate_v4(),'bga','Blood and glucose analyser',38),
    (uuid_generate_v4(),'chu','Reagents Strip for Urine - Chemistry',39),
    (uuid_generate_v4(),'his','Histopathology',40),
    (uuid_generate_v4(),'bfi','Bacterial and Fungal Isolation and Identification',41),
    (uuid_generate_v4(),'bsi','Bacterial Stains and Identification Agent',42),
    (uuid_generate_v4(),'pry','Parasitology',43),
    (uuid_generate_v4(),'cgi','Consumables and General Items',44),
    (uuid_generate_v4(),'hep','Haematology Pentra 8',46),
    (uuid_generate_v4(),'fas','Facs Callibur',47),
    (uuid_generate_v4(),'btf','Blood Transfusion',48),
    (uuid_generate_v4(),'ant','Anti - Inflamatory',49),
    (uuid_generate_v4(),'antang','Anti-Angina Medicines',31),
    (uuid_generate_v4(),'gen','General Purposes, Laboratory and Disinfectants Items',51),
    (uuid_generate_v4(),'hospef','Hospital Equipment And Furniture ',52),
    (uuid_generate_v4(),'hosplub','Hospital Linen, Uniforms & Beddings Materials ',53),
    (uuid_generate_v4(),'hhw','Hospital Hollow Wares ',54),
    (uuid_generate_v4(),'patmgmnt','Patient Management Devices And Other Tools ',55),
    (uuid_generate_v4(),'psychoa','Psychotherapeutic Agents',56),
    (uuid_generate_v4(),'reud','Reusable Devices (Hospital Instruments) ',57),
    (uuid_generate_v4(),'atb','Anti-TB',25),
    (uuid_generate_v4(),'antac','Anti - Acid Medicines',58),
    (uuid_generate_v4(),'antdb','Anti - Diabetic Medicines',59),
    (uuid_generate_v4(),'anhc','Anti - Hypertensive and Cardiac Glycoside',60),
    (uuid_generate_v4(),'antp','Antipruritics',61),
    (uuid_generate_v4(),'cat','Catheters &Tubes',62),
    (uuid_generate_v4(),'DiureticMed','Diuretic Medicines',63),
    (uuid_generate_v4(),'MicroSerol','Microbiology and Serology Items',64),
    (uuid_generate_v4(),'Patmgmntdevice','Patient Management Devices And Other Tools',65),
    (uuid_generate_v4(),'psychothagent','Psychotherapeutic Agents',66),
    (uuid_generate_v4(),'steroid','Steroidal Anti-Inflammatory Medicines',67),
    (uuid_generate_v4(),'surgicalInst','Surgical Instruments & Medical Kits/Sets',68),
    (uuid_generate_v4(),'syrin','Syringes, Needles, Cannulas &  Administration Sets',69),
    (uuid_generate_v4(),'vit','Vitamins & Minerals',70),
    (uuid_generate_v4(),'wasteCollect','Waste Collection Bags',71),
    (uuid_generate_v4(),'dress','Dressing Material ',72),
    (uuid_generate_v4(),'ana','Anti-allergies and Medicines used Anaphylaxis and Shock',3),
    (uuid_generate_v4(),'cough','Cough Medicine',72),
    (uuid_generate_v4(),'NSAIDS','Anti-pyretics NSAIDS',73),
    (uuid_generate_v4(),'anschisto','Anti-Schistosomal',74),
    (uuid_generate_v4(),'screenm','Screen Master',75),
    (uuid_generate_v4(),'anlg','Analgesics',0),
    (uuid_generate_v4(),'antidote','Antidote',76),
    (uuid_generate_v4(),'hemamicro60','Hematology Micro 60',78),
    (uuid_generate_v4(),'amoe','Amoebicides',1),
    (uuid_generate_v4(),'lane','Local Anesthesia',2),
    (uuid_generate_v4(),'gane','General Anesthesia',3),
    (uuid_generate_v4(),'asth','Anti-Asthmatic and Cough Medicine',6),
    (uuid_generate_v4(),'Anti-Co','Anti-Coagulant & Antagonists',79),
    (uuid_generate_v4(),'Anti-Ep','Anti-Epileptics/Anticonvasants',80),
    (uuid_generate_v4(),'Anti-Ha','Anti-Haemorrhoids',81),
    (uuid_generate_v4(),'Anti-Py','Anti-Pyretics And Nsaids',82),
    (uuid_generate_v4(),'Gloves','Gloves & Other Protective Gears',83),
    (uuid_generate_v4(),'Ironde','Iron Deficiency Anaemias Medicines',84),
    (uuid_generate_v4(),'Liq','Liquid, Correcting Electrolytes and Acid/Base Disturbances',85),
    (uuid_generate_v4(),'Med-Diar','Medicines used in Diarrhoea',86),
    (uuid_generate_v4(),'Muscle-Relax','Muscle Relaxants , Cholineserase Inhibitors and Anticholinergic',87),
    (uuid_generate_v4(),'Narcoti','Narcotic & Antagonists',88),
    (uuid_generate_v4(),'Ophtham','Ophthamological Preparations',89),
    (uuid_generate_v4(),'SeraIm','Sera And Immunoglobulins',90),
    (uuid_generate_v4(),'X-RayC','X-Ray Chemicals And Reagents',91),
    (uuid_generate_v4(),'X-RayF','X-Ray Films',92),
    (uuid_generate_v4(),'ane','Anaesthetics',1),
    (uuid_generate_v4(),'hmind','Hematology Reagents for Mindray Machines BC 3200',99),
    (uuid_generate_v4(),'hsys','Haematology Reagents for Sysmex (KX21, KX 1000i/PoCH)',110),
    (uuid_generate_v4(),'gds','GIT drugs and Suppliments',122),
    (uuid_generate_v4(),'ecfvm','Electrolystes corection  Fluid, Vaccines and Immunoglobins',123),
    (uuid_generate_v4(),'adv','Antiretriviral Drugs -ARVs',124),
    (uuid_generate_v4(),'aaam','Anti-Allergies and anaphylaxis mediecine',114),
    (uuid_generate_v4(),'ast','Asthma',115),
    (uuid_generate_v4(),'anhs','Antihelminthes',116),
    (uuid_generate_v4(),'anfs','Anti-Fungals',117),
    (uuid_generate_v4(),'antd','Anti-Diabetics',118),
    (uuid_generate_v4(),'ahcg','Anti - Hypertensive and Cardiac Glycoside.',119),
    (uuid_generate_v4(),'amm','Anti-Malarial medicine',120),
    (uuid_generate_v4(),'cnsm','Central Nervous System Medicines',121),
    (uuid_generate_v4(),'agmr','Anesthetics General and Muscle relaxants',112),
    (uuid_generate_v4(),'anta','Antibiotics and Amoebicides',113),
    (uuid_generate_v4(),'laa','Local Anaesthetics and analgesics',111),
    (uuid_generate_v4(),'nar','Narcotics',126),
    (uuid_generate_v4(),'fpsmp','Family Planning and Safe Motherhood product',127),
    (uuid_generate_v4(),'ansd','Antiseptics/Disinfectants.',128),
    (uuid_generate_v4(),'Ophthamp','Ophthalmic Preparation.',129),
    (uuid_generate_v4(),'msus','Medical supplies and utensils.',130),
    (uuid_generate_v4(),'nmv','Nutritions (Minerals and Vitamins)',131),
    (uuid_generate_v4(),'sergy','Serology.',132),
    (uuid_generate_v4(),'hbtr','Hematology and Blood transfusion',133),
    (uuid_generate_v4(),'chemy','Chemistry.',134),
    (uuid_generate_v4(),'icd','Immunophenotyping/CD4',135),
    (uuid_generate_v4(),'cosu','Consumables',136),
    (uuid_generate_v4(),'lmt','LMIS and Management Tools',137),
    (uuid_generate_v4(),'tbli','TB&LEPROSY ITEMS',138),
    (uuid_generate_v4(),'ctfc','CD-4 Testing Facs Count.',201),
    (uuid_generate_v4(),'vrl','Viral Load.',202),
    (uuid_generate_v4(),'htlgy','Haematology.',203),
    (uuid_generate_v4(),'htlgym','Haematology Micro 60.',204),
    (uuid_generate_v4(),'htlgyrfs','Haematology Reagents for Sysmex (KX21, KX 1000i/PoCH).',205),
    (uuid_generate_v4(),'htlgyrfmm','Haematology Reagents for Mindray Machines BC 3200.',206),
    (uuid_generate_v4(),'bctrg','Bacteriology (Stains and Bacterial ID agents).',207),
    (uuid_generate_v4(),'lur','Laboratory utensils/reagents.',208),
    (uuid_generate_v4(),'rsfuc','Reagents Strip for urine - Chemistry.',209),
    (uuid_generate_v4(),'cgs','Consumables and General items.',211),
    (uuid_generate_v4(),'scc','Specimen Collection/Consumables',45),
    (uuid_generate_v4(),'sccnew','Specimen Collection/Consumables.',210),
    (uuid_generate_v4(),'hmp','Haematology Pentra 80.',212),
    (uuid_generate_v4(),'dmn','Dawa za Magonjwa Nyemelezi',125);



insert into referencedata.dispensables
(id,type) values
(uuid_generate_v4(),'default');

insert into referencedata.dispensable_attributes
(dispensableid,key,value) values
((select id from referencedata.dispensables limit 1),'dispensingUnit','tablet');

--Insert products
insert into referencedata.orderables
(id, code, fullproductName, description, netcontent, packRoundingThreshold, roundToZero,dispensableid,versionid) values
('b477a8e4-056f-4506-ab57-c7ef5f6afd33','10010129AC','Zinc Sulphate','Zinc Sulphate',100,1,'true',(select id from referencedata.dispensables limit 1),1),
('b327c607-5a87-415d-8c62-daff58def51a','10010031MD','Griseofulvin',NULL,1000,1,'true',(select id from referencedata.dispensables limit 1),1),
('72c581bd-93dd-43e2-b9e0-4b13002fdf97','10010222SC','LOSARTAN 50MG TABLETS','LOSARTAN 50MG TABLETS',28,1,'true',(select id from referencedata.dispensables limit 1),1),
('3bf36a15-b276-4c14-ba58-b63ed02cd5d7','10010190SP','LORATIDINE 10MG TABLETS','LORATIDINE 10MG TABLETS',30,1,'true',(select id from referencedata.dispensables limit 1),1),
('dcca56dc-dbeb-4454-a70c-f84afcbb12f6','10010190MD','Loratidine Tablet',NULL,30,1,'true',(select id from referencedata.dispensables limit 1),1);



--Insert program_products
insert into referencedata.program_orderables(id, programId, orderableid, fullSupply, priceperpack, active, displayOrder, orderabledisplaycategoryId, orderableversionid) values
(uuid_generate_v4(),(select id from referencedata.programs where code='ilshosp'),(select id from referencedata.orderables where code = '10010129AC'),'true',0.00,'true',455,(select id from referencedata.orderable_display_categories where code='cgi'),1),
(uuid_generate_v4(),(select id from referencedata.programs where code='ilshosp'),(select id from referencedata.orderables where code = '10010031MD'),'true',0.00,'true',0,(select id from referencedata.orderable_display_categories where code='cgi'),1),
(uuid_generate_v4(),(select id from referencedata.programs where code='ilshosp'),(select id from referencedata.orderables where code = '10010222SC'),'true',6300.00,'true',560,(select id from referencedata.orderable_display_categories where code='cgi'),1),
(uuid_generate_v4(),(select id from referencedata.programs where code='ilshosp'),(select id from referencedata.orderables where code = '10010190SP'),'true',2600.00,'true',9,(select id from referencedata.orderable_display_categories where code='cgi'),1),
(uuid_generate_v4(),(select id from referencedata.programs where code='ilshosp'),(select id from referencedata.orderables where code = '10010190MD'),'true',133300.00,'true',356,(select id from referencedata.orderable_display_categories where code='cgi'),1);





--Insert facility_approved_products
insert into referencedata.facility_type_approved_products(id, facilityTypeId, programid, orderableid, maxperiodsOfStock, orderableversionid) values
(uuid_generate_v4(),(select id from referencedata.facility_types where code='disp'), (select id from referencedata.programs where code='ilshosp'),
 (select id from referencedata.orderables where  code='10010129AC'), 3, 1),
(uuid_generate_v4(),(select id from referencedata.facility_types where code='disp'), (select id from referencedata.programs where code='ilshosp'),
(select id from referencedata.orderables where  code='10010031MD'), 3, 1),
(uuid_generate_v4(),(select id from referencedata.facility_types where code='disp'), (select id from referencedata.programs where code='ilshosp'),
(select id from referencedata.orderables where  code='10010222SC'), 3, 1),
(uuid_generate_v4(),(select id from referencedata.facility_types where code='disp'), (select id from referencedata.programs where code='ilshosp'),
(select id from referencedata.orderables where  code='10010190SP'), 3, 1),
(uuid_generate_v4(),(select id from referencedata.facility_types where code='heac'), (select id from referencedata.programs where code='ilshosp'),
(select id from referencedata.orderables where  code='10010129AC'), 3, 1),
(uuid_generate_v4(),(select id from referencedata.facility_types where code='heac'), (select id from referencedata.programs where code='ilshosp'),
(select id from referencedata.orderables where  code='10010031MD'), 3, 1),
(uuid_generate_v4(),(select id from referencedata.facility_types where code='heac'), (select id from referencedata.programs where code='ilshosp'),
(select id from referencedata.orderables where  code='10010222SC'), 3, 1),
(uuid_generate_v4(),(select id from referencedata.facility_types where code='heac'), (select id from referencedata.programs where code='ilshosp'),
(select id from referencedata.orderables where  code='10010190SP'), 3, 1);







INSERT into referencedata.roles(id, name, description) VALUES
    (uuid_generate_v4(), 'Create & Submit','Can only create R&R'),
    (uuid_generate_v4(), 'Approve Only','Can only approve the R&R'),
    (uuid_generate_v4(), 'Authorize only','Can only authorize R&R'),
    (uuid_generate_v4(), 'Admin', 'Admin');

INSERT into referencedata.role_rights
(roleId, rightid) VALUES
  ((SELECT  id from referencedata.roles WHERE name = 'Create & Submit'), (select id from referencedata.rights where name = 'REQUISITION_VIEW')),
  ((SELECT  id  from referencedata.roles WHERE name = 'Create & Submit'), (select id from referencedata.rights where name = 'REQUISITION_CREATE')),
  ((SELECT id from referencedata.roles WHERE name = 'Create & Submit'), (select id from referencedata.rights where name = 'REQUISITION_DELETE')),
  ((SELECT id from referencedata.roles WHERE name = 'Approve Only'), (select id from referencedata.rights where name = 'REQUISITION_APPROVE')),
  ((SELECT id from referencedata.roles WHERE name = 'Approve Only'), (select id from referencedata.rights where name = 'REQUISITION_VIEW')),

  ((SELECT id from referencedata.roles WHERE name = 'Authorize only'), (select id from referencedata.rights where name = 'REQUISITION_AUTHORIZE')),
  ((SELECT id from referencedata.roles WHERE name = 'Authorize only'), (select id from referencedata.rights where name = 'REQUISITION_VIEW')),
  ((SELECT id from referencedata.roles WHERE name = 'Authorize only'), (select id from referencedata.rights where name = 'REQUISITION_CREATE')),
   ((SELECT id from referencedata.roles WHERE name = 'Authorize only'), (select id from referencedata.rights where name = 'REQUISITION_DELETE')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'FACILITIES_MANAGE')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'USER_ROLES_MANAGE')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'PROGRAMS_MANAGE')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'PROCESSING_SCHEDULES_MANAGE')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'REQUISITION_TEMPLATES_MANAGE')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'USERS_MANAGE')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'REPORTS_VIEW')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'REPORT_TEMPLATES_EDIT')),
((SELECT id from referencedata.roles WHERE name = 'Admin'), (select id from referencedata.rights where name = 'SYSTEM_SETTINGS_MANAGE'))
;


--Insert into referencedata.user table

INSERT into referencedata.users
(id, userName, homefacilityId, firstName, lastName, email, verified, active) VALUES
  (uuid_generate_v4(), 'StoreInCharge',
   (SELECT
      id
    from referencedata.facilities
    WHERE code = 'MZ520495'), 'Hassan', 'Matoto', 'Fatima_Doe@openlmis.com', TRUE, TRUE),

  (uuid_generate_v4(), 'FacilityInCharge',
   (SELECT
      id
    from referencedata.facilities
    WHERE code = 'MZ520495'), 'Rachel', 'Stephen', 'Jane_Doe@openlmis.com', TRUE, TRUE),

  (uuid_generate_v4(), 'lmu',
   (SELECT
      id
    from referencedata.facilities
    WHERE code = 'MZ520495'), 'Evance', 'Nkya', 'Frank_Doe@openlmis.com', TRUE, TRUE),
    (uuid_generate_v4(), 'Admin123', (SELECT
      id
    from referencedata.facilities
    WHERE code = 'MZ520495'), 'John', 'Doe', 'John_Doe@openlmis.com', TRUE, TRUE);




--Insert supervisory nodes


INSERT into referencedata.supervisory_nodes
(id, facilityId, name, code, parentId) VALUES
  ('7b2d5193-f2fa-45a1-83f0-524beaedbbc4', (SELECT id from referencedata.facilities WHERE code = 'MZ520495'), 'Mwanza Zone', 'MWAN-SNZ', NULL);




--Insert role assignments

INSERT into referencedata.role_assignments
(type, id, userId, roleId, programId, supervisoryNodeId) VALUES
  ('supervision', uuid_generate_v4(), (SELECT ID from referencedata.USERS WHERE username = 'StoreInCharge'), (SELECT id from referencedata.roles WHERE name = 'Create & Submit'), (SELECT id from referencedata.programs WHERE code = 'ilshosp'), NULL),
  ('supervision', uuid_generate_v4(), (SELECT ID from referencedata.USERS WHERE username = 'lmu'), (SELECT id from referencedata.roles WHERE name = 'Approve Only'), (SELECT id from referencedata.programs WHERE code = 'ilshosp'), (select id from referencedata.supervisory_nodes where code='MWAN-SNZ')),
  ('supervision', uuid_generate_v4(), (SELECT ID from referencedata.USERS WHERE username = 'FacilityInCharge'), (SELECT id from referencedata.roles WHERE name = 'Authorize only'), (SELECT id from referencedata.programs WHERE code = 'ilshosp'), NULL),
  ('direct', uuid_generate_v4(), (SELECT id from referencedata.users WHERE userName = 'Admin123'), (SELECT id from referencedata.roles WHERE name = 'Admin'), (SELECT id from referencedata.programs WHERE code = 'ilshosp'), NULL);









INSERT INTO referencedata.processing_schedules (id, code, name, description) VALUES
 (uuid_generate_v4(), 'groupA','BiMonthly Group A','Reporting and Ordering Group A'),
 (uuid_generate_v4(), 'groupB','BiMonthly Group B','Reporting and Ordering Group B');



INSERT INTO referencedata.processing_periods
(id, name, description, startDate, endDate, processingscheduleId) VALUES
    ('70f5bef1-9755-4f15-8933-d1b02cf69e2d','Jan 2019',NULL,'2019-01-01T00:00:00.000+03:00','2019-01-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('297bf315-1dce-4fbc-9d6d-7a572d2b213c','Jan -  Feb 2019',NULL,'2019-01-01T00:00:00.000+03:00','2019-02-28T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('1bdc180f-d1f3-4e71-9150-4ce244d1c97b','March 2019',NULL,'2019-03-01T00:00:00.000+03:00','2019-03-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('ac867e52-e468-48da-afe7-08ef3836a979','March - April 2019',NULL,'2019-03-01T00:00:00.000+03:00','2019-04-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('a48d5a86-683f-423b-99a2-2451afcef1c9','May 2019',NULL,'2019-05-01T00:00:00.000+03:00','2019-05-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('dee28f2b-390e-4253-b571-df71b5565f32','May - June 2019',NULL,'2019-05-01T00:00:00.000+03:00','2019-06-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('e13960d3-7b5e-403e-8796-0cc1f58d791c','July 2019',NULL,'2019-07-01T00:00:00.000+03:00','2019-07-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('cdd91765-8471-4f14-a708-64a15123ee38','July - August 2019',NULL,'2019-07-01T00:00:00.000+03:00','2019-08-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('fbbb45e5-069b-4938-8d9b-eff7d659eea1','Sep 2019',NULL,'2019-09-01T00:00:00.000+03:00','2019-09-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('6d903e0c-16e0-422c-b6d8-ecc00067f3a0','Sep - Oct 2019',NULL,'2019-09-01T00:00:00.000+03:00','2019-10-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('d5d1a17d-f52e-45c8-b8ea-953611b5216b','Nov 2019',NULL,'2019-11-01T00:00:00.000+03:00','2019-11-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('e5b04c19-cd68-45a1-a695-c8c6950f9773','Nov - Dec 2019',NULL,'2019-11-01T00:00:00.000+03:00','2019-12-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupA')),
    ('fa3fc1a1-72eb-40e5-95ba-c56bbd7c2036','Dec - Jan 2019',NULL,'2018-12-01T00:00:00.000+03:00','2019-01-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('9cf0e924-fd69-47b2-8adc-7e6a0839e576','Feb 2019',NULL,'2019-02-01T00:00:00.000+03:00','2019-02-28T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('41c668fd-3a48-4472-8c20-bd1570494104','Feb - March 2019',NULL,'2019-02-01T00:00:00.000+03:00','2019-03-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('eff7fe10-1a2a-4092-81cd-07755f530475','April 2019',NULL,'2019-04-01T00:00:00.000+03:00','2019-04-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('948810b0-eb80-417a-9e6c-e14cf372ce5e','April - May 2019',NULL,'2019-04-01T00:00:00.000+03:00','2019-05-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('2ca220da-93fa-4036-a54f-314d03d81a68','June 2019',NULL,'2019-06-01T00:00:00.000+03:00','2019-06-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('2c3b5a06-1a13-4a72-9222-c3e87fbe3a58','June - July 2019',NULL,'2019-06-01T00:00:00.000+03:00','2019-07-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('a9f795a3-0e3b-45d8-b6c3-90d6c3ea88de','Aug 2019',NULL,'2019-08-01T00:00:00.000+03:00','2019-08-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('e72f8b82-fb3c-4117-af0f-98f2e8070be0','Aug - Sept 2019',NULL,'2019-08-01T00:00:00.000+03:00','2019-09-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('21e04a55-7a28-41df-b391-01892bf8414f','Oct 2019',NULL,'2019-10-01T00:00:00.000+03:00','2019-10-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('be13048b-d2cc-4a25-bd45-f44f5480acdc','Oct - Nov 2019',NULL,'2019-10-01T00:00:00.000+03:00','2019-11-30T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB')),
    ('10c9b4d8-3952-4d1d-9300-741b27cadef6','Dec 2019',NULL,'2019-12-01T00:00:00.000+03:00','2019-12-31T23:59:59.000+03:00',(SELECT id from referencedata.processing_schedules WHERE code = 'groupB'));







INSERT into referencedata.requisition_groups (id, code ,name,description,supervisoryNodeId )values
(uuid_generate_v4(), 'DARE-RGILS15A','Morogoro MC - ILS - A','Morogoro MC Requisition Group A - ILS',(select id from referencedata.supervisory_nodes where code='MWAN-SNZ'));

INSERT into referencedata.requisition_group_members ( requisitionGroupId ,facilityId )values
((select id from referencedata. requisition_groups where code ='DARE-RGILS15A'),(select id from referencedata. facilities where code ='MZ520495')),
((select id from referencedata. requisition_groups where code ='DARE-RGILS15A'),(select id from referencedata. facilities where code ='MZ510054'));



insert into referencedata.requisition_group_program_schedules (id, requisitionGroupId , programId , processingscheduleId , directDelivery ) values
(uuid_generate_v4(), (select id from referencedata.requisition_groups where code='DARE-RGILS15A'),(select id from referencedata.programs where code='ilshosp'),(select id from referencedata.processing_schedules where code='groupA'),TRUE);





insert into requisition.requisition_templates (id, name, archived, populatestockonhandfromstockcards) values
('a2ade107-dcac-4e34-ae53-1dcfc6f39d5d', 'ILS redesigned template', false, false);

insert into requisition.requisition_template_assignments (id, templateid, programid, facilityTypeId) values
(uuid_generate_v4(), (select id from requisition.requisition_templates where name='ILS redesigned template'), (select id from referencedata.programs where code='ilshosp'), (select id from referencedata.facility_types where code ='disp'));



insert into requisition.columns_maps
(requisitioncolumnid, key, requisitioncolumnoptionid, requisitiontemplateid, isdisplayed, source, displayOrder, label)
values
    ((select id from requisition.available_requisition_columns where name = 'skipped'),'skipped',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,1,'Skip'),
    ((select id from requisition.available_requisition_columns where name = 'orderable.productCode'),'orderable.productCode',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',2,2,'Namba mpya ya MSD'),
    ((select id from requisition.available_requisition_columns where name = 'orderable.fullProductName'),'orderable.fullProductName',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',2,3,'Maelezo ya bidhaa'),
    ((select id from requisition.available_requisition_columns where name = 'orderable.dispensable.displayUnit'),'orderable.dispensable.displayUnit',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',2,4,'Kipimo cha Ugavi (U)'),
    ((select id from requisition.available_requisition_columns where name = 'beginningBalance'),'beginningBalance',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,5,'Kiasi cha kuanzia (A)'),
    ((select id from requisition.available_requisition_columns where name = 'totalReceivedQuantity'),'totalReceivedQuantity',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,6,'Kiasi kilichopokelewa (B)'),
    ((select id from requisition.available_requisition_columns where name = 'totalLossesAndAdjustments'),'totalLossesAndAdjustments',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,7,'Upotevu au Marekebisho (C)'),
    ((select id from requisition.available_requisition_columns where name = 'totalStockoutDays'),'totalStockoutDays',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,8,'Siku Ambazo Dawa Haikuwepo Kituoni (X)'),
    ((select id from requisition.available_requisition_columns where name = 'stockOnHand'),'stockOnHand',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,9,'Salio la mwisho (D)'),
    ((select id from requisition.available_requisition_columns where name = 'totalConsumedQuantity'),'totalConsumedQuantity',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,10,'Matumizi halisi (E)'),
    ((select id from requisition.available_requisition_columns where name = 'adjustedConsumption'),'adjustedConsumption',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',1,11,'Makadirio ya matumizi baada ya marekebisho [E* 60]/]60-X] (Z)'),
    ((select id from requisition.available_requisition_columns where name = 'maximumStockQuantity'),'maximumStockQuantity',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',1,12,'Kiasi cha juu kinachohitajika [Z x 2] (Y)'),
    ((select id from requisition.available_requisition_columns where name = 'calculatedOrderQuantity'),'calculatedOrderQuantity',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',1,13,'Kiasi kinachohitajika kilichokokotolewa [Y - D] (F)'),
    ((select id from requisition.available_requisition_columns where name = 'requestedQuantity'),'requestedQuantity',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,14,'Kiasi halisi kinachohitajika (G)'),
    ((select id from requisition.available_requisition_columns where name = 'packsToShip'),'packsToShip',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',1,15,'Kiasi cha kuagizwa [K/U] (H)'),
    ((select id from requisition.available_requisition_columns where name = 'remarks'),'remarks',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,16,'Maelezo ya kiasi kilichoidhinishwa'),
    ((select id from requisition.available_requisition_columns where name = 'requestedQuantityExplanation'),'requestedQuantityExplanation',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,17,'Maelezo ya kiasi halisi kinachohitajika'),
    ((select id from requisition.available_requisition_columns where name = 'pricePerPack'),'pricePerPack',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',2,18,'Bei (I)'),
    ((select id from requisition.available_requisition_columns where name = 'totalCost'),'totalCost',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',1,19,'Gharama [H x I] (J)'),
    ((select id from requisition.available_requisition_columns where name = 'approvedQuantity'),'approvedQuantity',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'true',0,20,'Kiasi kilichoindinishwa (K)'),
    ((select id from requisition.available_requisition_columns where name = 'total'),'total',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'false',1,22,'Total'),
    ((select id from requisition.available_requisition_columns where name = 'averageConsumption'),'averageConsumption',NULL,(select id from requisition.requisition_templates where name='ILS redesigned template'),'false',1,23,'Average Monthly Consumption(AMC)'),
    ((select id from requisition.available_requisition_columns where name = 'numberOfNewPatientsAdded'),'numberOfNewPatientsAdded',(select id from requisition.available_requisition_column_options where optionname = 'newPatientCount'),(select id from requisition.requisition_templates where name='ILS redesigned template'),'false',0,24,'Total number of new patients added to service on the program')
;

insert into auth.auth_users (id,enabled,password,username) values
((select id from referencedata.users where username = 'StoreInCharge'),TRUE,'$2a$10$4IZfidcJzbR5Krvj87ZJdOZvuQoD/kvPAJe549rUNoP3N3uH0Lq2G','StoreInCharge'),
((select id from referencedata.users where username = 'FacilityInCharge'),TRUE,'$2a$10$4IZfidcJzbR5Krvj87ZJdOZvuQoD/kvPAJe549rUNoP3N3uH0Lq2G','FacilityInCharge'),
((select id from referencedata.users where username = 'lmu'),TRUE,'$2a$10$4IZfidcJzbR5Krvj87ZJdOZvuQoD/kvPAJe549rUNoP3N3uH0Lq2G','lmu'),
((select id from referencedata.users where username = 'Admin123'),TRUE,'$2a$10$4IZfidcJzbR5Krvj87ZJdOZvuQoD/kvPAJe549rUNoP3N3uH0Lq2G','Admin123');

insert into notification.user_contact_details (referenceDataUserId,phoneNumber,email,allowNotify,emailVerified) values
((select id from referencedata.users where username = 'StoreInCharge'),NULL,'Fatima_Doe@openlmis.com','false','true'),
((select id from referencedata.users where username = 'FacilityInCharge'),NULL,'Jane_Doe@openlmis.com','false','true'),
((select id from referencedata.users where username = 'lmu'),NULL,'Frank_Doe@openlmis.com','false','true'),
((select id from referencedata.users where username = 'Admin123'),NULL,'John_Doe@openlmis.com','false','true');
