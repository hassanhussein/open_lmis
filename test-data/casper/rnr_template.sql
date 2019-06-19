--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--

ALTER TABLE program_rnr_columns DISABLE TRIGGER ALL;
delete from program_rnr_columns;
ALTER TABLE program_rnr_columns ENABLE TRIGGER ALL;



insert into program_rnr_columns
(masterColumnId, rnrOptionId, programId, visible, source, position, label)
values
    (1,NULL,(select id from programs where code='ilshosp'),'true','U',1,'Skip'),
    (2,NULL,(select id from programs where code='ilshosp'),'true','R',2,'Namba mpya ya MSD'),
    (3,NULL,(select id from programs where code='ilshosp'),'true','R',3,'Maelezo ya bidhaa'),
    (4,NULL,(select id from programs where code='ilshosp'),'true','R',4,'Kipimo cha Ugavi (U)'),
    (5,NULL,(select id from programs where code='ilshosp'),'true','U',5,'Kiasi cha kuanzia (A)'),
    (6,NULL,(select id from programs where code='ilshosp'),'true','U',6,'Kiasi kilichopokelewa (B)'),
    (9,NULL,(select id from programs where code='ilshosp'),'true','U',7,'Upotevu au Marekebisho (C)'),
    (12,NULL,(select id from programs where code='ilshosp'),'true','U',8,'Siku Ambazo Dawa Haikuwepo Kituoni (X)'),
    (10,NULL,(select id from programs where code='ilshosp'),'true','U',9,'Salio la mwisho (D)'),
    (8,NULL,(select id from programs where code='ilshosp'),'true','U',10,'Matumizi halisi (E)'),
    (13,NULL,(select id from programs where code='ilshosp'),'true','C',11,'Makadirio ya matumizi baada ya marekebisho [E* 60]/]60-X] (Z)'),
    (15,NULL,(select id from programs where code='ilshosp'),'true','C',12,'Kiasi cha juu kinachohitajika [Z x 2] (Y)'),
    (16,NULL,(select id from programs where code='ilshosp'),'true','C',13,'Kiasi kinachohitajika kilichokokotolewa [Y - D] (F)'),
    (17,NULL,(select id from programs where code='ilshosp'),'true','U',14,'Kiasi halisi kinachohitajika (G)'),
    (20,NULL,(select id from programs where code='ilshosp'),'true','C',15,'Kiasi cha kuagizwa [K/U] (H)'),
    (24,NULL,(select id from programs where code='ilshosp'),'true','U',16,'Maelezo ya kiasi kilichoidhinishwa'),
    (18,NULL,(select id from programs where code='ilshosp'),'true','U',17,'Maelezo ya kiasi halisi kinachohitajika'),
    (21,NULL,(select id from programs where code='ilshosp'),'true','R',18,'Bei (I)'),
    (22,NULL,(select id from programs where code='ilshosp'),'true','C',19,'Gharama [H x I] (J)'),
    (19,NULL,(select id from programs where code='ilshosp'),'true','U',20,'Kiasi kilichoindinishwa (K)'),
    (23,NULL,(select id from programs where code='ilshosp'),'false','U',21,'Expiration Date'),
    (7,NULL,(select id from programs where code='ilshosp'),'false','C',22,'Total'),
    (14,NULL,(select id from programs where code='ilshosp'),'false','C',23,'Average Monthly Consumption(AMC)'),
    (11,1,(select id from programs where code='ilshosp'),'false','U',24,'Total number of new patients added to service on the program'),
    (25,NULL,(select id from programs where code='ilshosp'),'false','C',25,'Period Normalized Consumption');
;


update programs set templateConfigured = true where id in ((select id from programs where code = 'ilshosp'));
