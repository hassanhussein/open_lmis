--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--


ALTER TABLE processing_schedules DISABLE TRIGGER ALL;
ALTER TABLE processing_periods DISABLE TRIGGER ALL;

DELETE FROM processing_schedules;
DELETE FROM processing_periods;

ALTER TABLE processing_schedules ENABLE TRIGGER ALL;
ALTER TABLE processing_periods ENABLE TRIGGER ALL;

INSERT INTO processing_schedules (code, name, description) VALUES
 ('groupA','BiMonthly Group A','Reporting and Ordering Group A'),
 ('groupB','BiMonthly Group B','Reporting and Ordering Group B');



INSERT INTO processing_periods
(name, description, startDate, endDate, numberOfMonths, scheduleId, modifiedBy, enableorder ,isreporting) VALUES
    ('Jan 2019',NULL,'2019-01-01T00:00:00.000+03:00','2019-01-31T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),false,true),
    ('Jan -  Feb 2019',NULL,'2019-01-01T00:00:00.000+03:00','2019-02-28T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),true,false),
    ('March 2019',NULL,'2019-03-01T00:00:00.000+03:00','2019-03-31T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),false,true),
    ('March - April 2019',NULL,'2019-03-01T00:00:00.000+03:00','2019-04-30T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),true,false),
    ('May 2019',NULL,'2019-05-01T00:00:00.000+03:00','2019-05-31T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),false,true),
    ('May - June 2019',NULL,'2019-05-01T00:00:00.000+03:00','2019-06-30T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),true,false),
    ('July 2019',NULL,'2019-07-01T00:00:00.000+03:00','2019-07-31T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),false,true),
    ('July - August 2019',NULL,'2019-07-01T00:00:00.000+03:00','2019-08-31T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),true,false),
    ('Sep 2019',NULL,'2019-09-01T00:00:00.000+03:00','2019-09-30T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),false,true),
    ('Sep - Oct 2019',NULL,'2019-09-01T00:00:00.000+03:00','2019-10-31T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),true,false),
    ('Nov 2019',NULL,'2019-11-01T00:00:00.000+03:00','2019-11-30T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),false,true),
    ('Nov - Dec 2019',NULL,'2019-11-01T00:00:00.000+03:00','2019-12-31T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupA'), (SELECT id FROM users LIMIT 1),true,false),
    ('Dec - Jan 2019',NULL,'2018-12-01T00:00:00.000+03:00','2019-01-31T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),true,false),
    ('Feb 2019',NULL,'2019-02-01T00:00:00.000+03:00','2019-02-28T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),false,true),
    ('Feb - March 2019',NULL,'2019-02-01T00:00:00.000+03:00','2019-03-31T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),true,false),
    ('April 2019',NULL,'2019-04-01T00:00:00.000+03:00','2019-04-30T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),false,true),
    ('April - May 2019',NULL,'2019-04-01T00:00:00.000+03:00','2019-05-31T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),true,false),
    ('June 2019',NULL,'2019-06-01T00:00:00.000+03:00','2019-06-30T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),false,true),
    ('June - July 2019',NULL,'2019-06-01T00:00:00.000+03:00','2019-07-31T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),true,false),
    ('Aug 2019',NULL,'2019-08-01T00:00:00.000+03:00','2019-08-31T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),false,true),
    ('Aug - Sept 2019',NULL,'2019-08-01T00:00:00.000+03:00','2019-09-30T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),true,false),
    ('Oct 2019',NULL,'2019-10-01T00:00:00.000+03:00','2019-10-31T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),false,true),
    ('Oct - Nov 2019',NULL,'2019-10-01T00:00:00.000+03:00','2019-11-30T23:59:59.000+03:00',2,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),true,false),
    ('Dec 2019',NULL,'2019-12-01T00:00:00.000+03:00','2019-12-31T23:59:59.000+03:00',1,(SELECT id FROM processing_schedules WHERE code = 'groupB'), (SELECT id FROM users LIMIT 1),false,true);