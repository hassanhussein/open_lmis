--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--



ALTER TABLE facility_types DISABLE TRIGGER ALL;
ALTER TABLE programs_supported DISABLE TRIGGER ALL;
ALTER TABLE facilities DISABLE TRIGGER ALL;
ALTER TABLE facility_operators DISABLE TRIGGER ALL;
ALTER TABLE geographic_zones DISABLE TRIGGER ALL;
ALTER TABLE geographic_levels DISABLE TRIGGER ALL;

DELETE FROM facility_types;
DELETE FROM programs_supported;
DELETE FROM facilities;
DELETE FROM facility_operators;
DELETE FROM geographic_zones;
DELETE FROM geographic_levels;

ALTER TABLE facility_types ENABLE TRIGGER ALL;
ALTER TABLE programs_supported ENABLE TRIGGER ALL;
ALTER TABLE facilities ENABLE TRIGGER ALL;
ALTER TABLE facility_operators ENABLE TRIGGER ALL;
ALTER TABLE geographic_zones ENABLE TRIGGER ALL;
ALTER TABLE geographic_levels ENABLE TRIGGER ALL;



INSERT INTO geographic_levels
(code, name, levelNumber) VALUES
('country', 'Country', 1),
('state', 'State', 2),
('province', 'Province', 3),
('district', 'District', 4);

INSERT INTO geographic_zones
(code, name, levelId, parentId) values
('Root', 'Root', (SELECT id FROM geographic_levels WHERE code = 'country'), NULL);

INSERT INTO geographic_zones
(code, name, levelId, parentId) values
('Mozambique', 'Mozambique', (SELECT id FROM geographic_levels WHERE code = 'country'), NULL);


INSERT INTO geographic_zones
(code, name, levelId, parentId) values
('Arusha', 'Arusha',(SELECT id FROM geographic_levels WHERE code = 'state'), (SELECT id FROM geographic_zones WHERE code = 'Root'));


INSERT INTO geographic_zones
(code, name, levelId, parentId) values
('Dodoma', 'Dodoma',(SELECT id FROM geographic_levels WHERE code = 'province'), (SELECT id FROM geographic_zones WHERE code = 'Arusha'));

INSERT INTO geographic_zones
(code, name, levelId, parentId) values
('Ngorongoro', 'Ngorongoro', (SELECT id FROM geographic_levels WHERE code = 'district'), (SELECT id FROM geographic_zones WHERE code = 'Dodoma'));






INSERT INTO facility_operators
(code,      text,      displayOrder) VALUES
('MoH',     'MoH',     1),
('NGO',     'NGO',     2),
('FBO',     'FBO',     3),
('Private', 'Private', 4);



INSERT INTO facility_types (code, name, description, levelId, nominalMaxMonth, nominalEop, displayOrder, active) VALUES
    ('disp','Dispensary','Dispensary',5,6,3.00,13,'true'),
    ('heac','Health Centre','Health Centre',5,6,3.00,14,'true'),
    ('ddho','District Designated Hospital','District Designated Hospital',4,6,3.00,10,'true');

--Insert Facilities Section
INSERT INTO facilities
( code, name, description, gln, mainphone, fax, address1, address2, geographiczoneid, typeid, catchmentpopulation, latitude,
longitude, altitude, operatedbyid, coldstoragegrosscapacity, coldstoragenetcapacity,suppliesothers, sdp, online,
 satellite, parentfacilityid, haselectricity, haselectronicscc, haselectronicdar, active, golivedate, godowndate,
  comment, enabled, virtualfacility, createdby, createddate, modifiedby,
            modifieddate, pricescheduleid)
            VALUES
    ('MZ520495','Kibirizi','Dispensary','G17786',2552603988,'null',NULL,NULL,(select id from geographic_zones where code ='Ngorongoro'),(select id from facility_types where code ='disp'),10000,NULL,NULL,128.3000,(select id from facility_operators where code='MoH'),9.0000,9.0000,'false','true','true','false',NULL,'true','false','false','true','2013-01-01 00:00:00','2013-01-01 00:00:00',NULL,'true','false',2,'2013-10-18T13:49:29.277+03:00',45,'2019-03-04T07:00:32.973+03:00',NULL),
    ('MZ510054','Katoro Bukoba DC','Health Centre','G17799',2552604001,'null',NULL,NULL,(select id from geographic_zones where code ='Ngorongoro'),(select id from facility_types where code ='heac'),10000,NULL,NULL,128.3000,(select id from facility_operators where code='MoH'),9.0000,9.0000,'false','true','true','false',NULL,'true','false','false','true','2013-01-01 00:00:00','2013-01-01 00:00:00',NULL,'true','false',2,'2013-10-18T13:49:29.277+03:00',45,'2019-03-04T11:44:13.037+03:00',NULL);




--Insert Programs Supported

INSERT INTO programs_supported (facilityId, programId, startDate, active, modifiedBy) VALUES
  ((SELECT
      id
    FROM facilities
    WHERE code = 'MZ520495'),
     (SELECT
      id
    FROM programs
    WHERE code = 'ilshosp'), '11/11/12', TRUE, 1);

  INSERT INTO programs_supported (facilityId, programId, startDate, active, modifiedBy) VALUES
  ((SELECT
      id
    FROM facilities
    WHERE code = 'MZ510054'),
     (SELECT
      id
    FROM programs
    WHERE code = 'ilshosp'), '11/11/12', TRUE, 1);


