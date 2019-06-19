--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--

ALTER TABLE geographic_zones DISABLE TRIGGER ALL;
ALTER TABLE geographic_levels DISABLE TRIGGER ALL;

DELETE FROM geographic_zones;
DELETE FROM geographic_levels;

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

