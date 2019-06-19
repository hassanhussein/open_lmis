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
DELETE FROM programs;
ALTER TABLE programs ENABLE TRIGGER ALL;

INSERT INTO programs (code, name, description, active, templateconfigured, regimentemplateconfigured,
            budgetingapplies, usesdar, push, sendfeed, createdby, createddate,
            modifiedby, modifieddate, isequipmentconfigured, hideskippedproducts,
            shownonfullsupplytab, enableskipperiod, enableivdform, usepriceschedule,
            enablemonthlyreporting)
  VALUES
    ('ilshosp','Redesigned ILS','ILS Hospital','true','true','false','true','false','false','true',1,'2014-10-02T10:02:18.980+03:00',1,'2014-10-02T10:02:18.980+03:00','false','true','true','false','false','false','true');
