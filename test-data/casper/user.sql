--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--


ALTER TABLE roles DISABLE TRIGGER ALL;
ALTER TABLE role_rights DISABLE TRIGGER ALL;
ALTER TABLE users DISABLE TRIGGER ALL;
ALTER TABLE supervisory_nodes DISABLE TRIGGER ALL;
ALTER TABLE role_assignments DISABLE TRIGGER ALL;
ALTER TABLE rights DISABLE TRIGGER ALL;

DELETE FROM roles;
DELETE FROM role_rights;
DELETE FROM users;
DELETE FROM supervisory_nodes;
DELETE FROM role_assignments;
DELETE FROM rights;

ALTER TABLE roles ENABLE TRIGGER ALL;
ALTER TABLE role_rights ENABLE TRIGGER ALL;
ALTER TABLE users ENABLE TRIGGER ALL;
ALTER TABLE supervisory_nodes ENABLE TRIGGER ALL;
ALTER TABLE role_assignments ENABLE TRIGGER ALL;
ALTER TABLE rights ENABLE TRIGGER ALL;

INSERT INTO rights (name, rightType, description) VALUES
    ('CREATE_LOT','REQUISITION','Permission to create lot'),
    ('VIEW_PIPELINE_EXPORT','REPORT','Permission to view Pipeline export Report'),
    ('OPEN_STOCK_RECEIVING_PROCESS','REQUISITION','Permission to create open stock receiving'),
    ('VIEW_ORDER_REPORT','REPORT','Permission to view Order Report'),
    ('CONFIGURE_RNR','ADMIN','Permission to create and edit r&r template for any program'),
    ('MANAGE_FACILITY','ADMIN','Permission to manage facilities(crud)'),
    ('MANAGE_ROLE','ADMIN','Permission to create and edit roles in the system'),
    ('MANAGE_SCHEDULE','ADMIN','Permission to create and edit schedules in the system'),
    ('MANAGE_USER','ADMIN','Permission to create and view users'),
    ('UPLOADS','ADMIN','Permission to upload'),
    ('VIEW_ORDER','FULFILLMENT','Permission to view orders'),
    ('VIEW_RNR_FEEDBACK_REPORT','REPORT','Permission to view Report and Requisition Feedback Report.'),
    ('COMPLETE_POD','REQUISITION','Permission to complete POD on behalf of facilities'),
    ('VIEW_ADJUSTMENT_SUMMARY_REPORT','REPORT','Permission to view adjustment summary Report'),
    ('VIEW_REPORTING_RATE_REPORT','REPORT','Permission to view Reporting Rate Report'),
    ('VIEW_AVERAGE_CONSUMPTION_REPORT','REPORT','Permission to view avergae consumption Report'),
    ('VIEW_SUPPLY_STATUS_REPORT','REPORT','Permission to view supply status by facility report'),
    ('VIEW_CONSUMPTION_REPORT','REPORT','Permission to view Consumption Report'),
    ('VIEW_DISTRICT_CONSUMPTION_REPORT','REPORT','Permission to view district consumption comparison report'),
    ('VIEW_MAILING_LABEL_REPORT','REPORT','Permission to view Mailing labels for Facilities'),
    ('VIEW_NON_REPORTING_FACILITIES','REPORT','Permission to view Non reporting Facility List Report'),
    ('VIEW_STOCKED_OUT_REPORT','REPORT','Permission to view stocked out commodity Report'),
    ('VIEW_FACILITY_REPORT','REPORT','Permission to view Facility List Report'),
    ('INITIALIZE_STOCK','REQUISITION','Permission to initialize stock'),
    ('VIEW_SUMMARY_REPORT','REPORT','Permission to view Summary Report'),
    ('VIEW_ORDER_FILL_RATE_REPORT','REPORT','Permission to view Order Fill Rate Report'),
    ('VIEW_REGIMEN_SUMMARY_REPORT','REPORT','Permission to view Regimen Summary Report.'),
    ('VIEW_DISTRICT_FINANCIAL_SUMMARY_REPORT','REPORT','Permission to view District Financial Summary Report.'),
    ('VIEW_REQUISITION','REQUISITION','Permission to view requisition'),
    ('CREATE_REQUISITION','REQUISITION','Permission to create, edit, submit and recall requisitions'),
    ('AUTHORIZE_REQUISITION','REQUISITION','Permission to edit, authorize and recall requisitions'),
    ('APPROVE_REQUISITION','REQUISITION','Permission to approve requisitions'),
    ('CONVERT_TO_ORDER','FULFILLMENT','Permission to convert requisitions to order'),
    ('MANAGE_PROGRAM_PRODUCT','ADMIN','Permission to manage program products'),
    ('MANAGE_DISTRIBUTION','ALLOCATION','Permission to manage an distribution'),
    ('SYSTEM_SETTINGS','ADMIN','System Settings'),
    ('MANAGE_REGIMEN_TEMPLATE','ADMIN','Permission to manage a regimen template'),
    ('FACILITY_FILL_SHIPMENT','FULFILLMENT','Permission to fill shipment data for facility'),
    ('MANAGE_POD','FULFILLMENT','Permission to manage POD'),
    ('MANAGE_GEOGRAPHIC_ZONE','ADMIN','Permission to manage geographic zones'),
    ('MANAGE_SUPPLY_LINE','ADMIN','Permission to manage supply lines'),
    ('MANAGE_FACILITY_APPROVED_PRODUCT','ADMIN','Permission to manage facility approved products'),
    ('MANAGE_REPORT','REPORTING','Permission to manage reports'),
    ('VIEW_DASHBOARD','REPORT','Permission to view dashboard poc'),
    ('VIEW_ELMIS_DASHBOARD','REPORT','Permission to view eLMIS dashboard poc'),
    ('MANAGE_DISTRICT_DEMOGRAPHIC_ESTIMATES','REQUISITION','Permission to manage district demographic estimates'),
    ('VIEW_DASHBOARD_POC','REPORT','Permission to view Dashboard POC'),
    ('VIEW_USER_SUMMARY_REPORT','REPORT','Permission to view user summary Report'),
    ('VIEW_STOCK_IMBALANCE_REPORT','REPORT','Permission to view Stock Imbalance Report.'),
    ('MANAGE_SETTING','ADMIN','Permission to configure settings.'),
    ('MANAGE_PRODUCT','ADMIN','Permission to manage products.'),
    ('MANAGE_REQUISITION_GROUP','ADMIN','Permission to manage requisition groups.'),
    ('MANAGE_SUPERVISORY_NODE','ADMIN','Permission to manage supervisory nodes.'),
    ('VIEW_LAB_EQUIPMENT_LIST_REPORT','REPORT','Permission to view lab equipment list Report'),
    ('VIEW_LAB_EQUIPMENTS_BY_FUNDING_SOURCE','REPORT','Permission to view lab equipment list by funding source Report'),
    ('VIEW_ORDER_FILL_RATE_SUMMARY_REPORT','REPORT','Permission to view order fill rate summary Report.'),
    ('VIEW_LAB_EQUIPMENTS_BY_LOCATION_REPORT','REPORT','Permission to view lab equipments by location Report'),
    ('VIEW_TIMELINESS_REPORT','REPORT','Permission to view Timeliness Report'),
    ('VIEW_SEASONALITY_RATIONING_REPORT','REPORT','Permission to view seasonality rationing Report'),
    ('VIEW_CCE_STORAGE_CAPACITY_REPORT','REPORT','Permission to view CCE Storage Capacity Report'),
    ('VIEW_COLD_CHAIN_EQUIPMENT_LIST_REPORT','REPORT','Permission to view cold chain equipment list Report'),
    ('VIEW_REPAIR_MANAGEMENT_REPORT','REPORT','Permission to view Repair Management Report'),
    ('VIEW_VACCINE_REPLACEMENT_PLAN_SUMMARY','REPORT','Permission to View Replacement Plan Summary Report'),
    ('MANAGE_CUSTOM_REPORTS','REPORT','Permission to manage custom reports'),
    ('ACCESS_NEW_DASHBOARD','REPORT','Permission to access new dashboard'),
    ('MANAGE_VACCINE_DISEASE_LIST','ADMIN','Permission to manage vaccine disease list'),
    ('MANAGE_SUPPLYLINE','ADMIN','Permission to create and edit Supply Line'),
    ('MANAGE_ELMIS_INTERFACE','ADMIN','Permission to manage ELMIS interface apps setting'),
    ('CONFIGURE_HELP_CONTENT','ADMIN','Permission to Configure Help Content'),
    ('ACCESS_ILS_GATEWAY','ADMIN','Permission to access the ILS Gateway.'),
    ('MANAGE_EQUIPMENT_SETTINGS','ADMIN','Permission to manage equipment settings'),
    ('SERVICE_VENDOR_RIGHT','ADMIN','Permission to use system as service Vendor'),
    ('MANAGE_DONOR','ADMIN','Permission to manage donors.'),
    ('MANAGE_SEASONALITY_RATIONING','ADMIN','Permission to manage seasonality rationing '),
    ('MANAGE_DEMOGRAPHIC_PARAMETERS','ADMIN','Permission to manage demographic parameters'),
    ('DELETE_REQUISITION','REQUISITION','Permission to delete requisitions'),
    ('MANAGE_EQUIPMENT_INVENTORY','REQUISITION','Permission to manage equipment inventory for each facility'),
    ('MANAGE_DEMOGRAPHIC_ESTIMATES','REQUISITION','Permission to manage demographic estimates'),
    ('FINALIZE_DEMOGRAPHIC_ESTIMATES','REQUISITION','Permission to finalize demographic estimates'),
    ('UNLOCK_FINALIZED_DEMOGRAPHIC_ESTIMATES','REQUISITION','Permission to unlock finalized demographic estimates'),
    ('CREATE_IVD','REQUISITION','Permission to create ivd form'),
    ('APPROVE_IVD','REQUISITION','Permission to Approve ivd form'),
    ('VIEW_IVD','REQUISITION','Permission to view ivd reports'),
    ('VIEW_STOCK_ON_HAND','REQUISITION','Permission to view stock on hand'),
    ('MANAGE_VACCINE_PRODUCTS_CONFIGURATION','ADMIN','Permission to manage vaccine product configuration'),
    ('VIEW_ORDER_REQUISITION','REQUISITION','Permission to view Order Requisition'),
    ('VIEW_PENDING_REQUEST','REQUISITION','Permission to View Pending Request'),
    ('CREATE_ORDER_REQUISITION','REQUISITION','Permission to Create Requisition'),
    ('MASS_DISTRIBUTION','REQUISITION','Permission to do mass distribution'),
    ('MANAGE_SUPERVISED_EQUIPMENTS','REQUISITION','Permission to manage equipment inventory for supervised facility'),
    ('MANAGE_STOCK','REQUISITION','Permission to manage stock (issue/receive/adjust)'),
    ('VIEW_REQUISITION_REPORT','REPORT','Permission to View Requisitions Report'),
    ('VIEW_STOCK_ON_HAND_REPORT','REPORT','Permission to View Stock On Hand Report'),
    ('VIEW_VACCINE_STOCK_STATUS_REPORT','REPORT','Permission to View Vaccine Stock Status Report'),
    ('VIEW_STOCK_LEDGER_REPORT','REPORT','Permission to View Stock Ledger Report'),
    ('VIEW_VACCINE_REPORT','REPORT','Permission to view vaccine report'),
    ('DELETE_EQUIPMENT_INVENTORY','ADMIN','Permission to delete equipment inventory'),
    ('USE_BARCODE_FEATURE','REQUISITION','Permission to use barcode functionality'),
    ('MANAGE_FACILITY_DISTRIBUTION','REQUISITION','Permission to manage facility Distribution'),
    ('VIEW_DISTRIBUTION_SUMMARY_REPORT','REPORT','Permission to view Distribution summary report'),
    ('MANAGE_MANUAL_TEST_TYPES','ADMIN','Permission to access manual test type'),
    ('VIEW_VIMS_NOTIFICATION','REPORT','Permission to view vims dashboard notification'),
    ('VIEW_VIMS_MAIN_DASHBOARD','REPORT','Permission to view Vims Main Dashboard'),
    ('VIEW_MSD_DASHLET_REPORT','REPORT','Permission to view MSD Dashboard'),
    ('VIEW_REPORT','REPORT','Permission to View Stock Stock Status Report'),
    ('VIEW_REDESIGNED_STOCK_STATUS_REPORT','REPORT','Permission to View Stock Stock Status Report');


--Insert into roles table
INSERT INTO roles( name, description, createdby, createddate, modifiedby, modifieddate) VALUES
    ('Create & Submit','Can only create R&R',2,'2015-07-10T11:58:51.071+03:00',2,'2015-07-10T11:58:51.071+03:00'),
    ('Approve Only','Can only approve the R&R',2,'2014-03-25T06:45:14.146+03:00',2,'2015-07-22T15:11:39.317+03:00'),
    ('Authorize only','Can only authorize R&R',2,'2015-07-21T15:37:32.213+03:00',45,'2018-10-08T08:36:40.328+03:00'),
    ('Admin', 'Admin',2,'2015-07-21T15:37:32.213+03:00',2,'2018-10-08T08:36:40.328+03:00');


--  Insert roles rights

INSERT INTO role_rights
(roleId, rightName) VALUES
  ((SELECT  id FROM roles WHERE name = 'Create & Submit'), 'VIEW_REQUISITION'),
  ((SELECT  id  FROM roles WHERE name = 'Create & Submit'), 'CREATE_REQUISITION'),
  ((SELECT id FROM roles WHERE name = 'Create & Submit'), 'DELETE_REQUISITION'),
  ((SELECT id FROM roles WHERE name = 'Approve Only'), 'APPROVE_REQUISITION'),
  ((SELECT id FROM roles WHERE name = 'Approve Only'), 'VIEW_REQUISITION'),

  ((SELECT id FROM roles WHERE name = 'Authorize only'), 'AUTHORIZE_REQUISITION'),
  ((SELECT id FROM roles WHERE name = 'Authorize only'), 'VIEW_REQUISITION'),
  ((SELECT id FROM roles WHERE name = 'Authorize only'), 'CREATE_REQUISITION'),
   ((SELECT id FROM roles WHERE name = 'Authorize only'), 'DELETE_REQUISITION'),
  ((SELECT id FROM roles WHERE name = 'Admin'), 'UPLOADS'),
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_FACILITY'),
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_ROLE'),
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_PROGRAM_PRODUCT'),
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_SCHEDULE'),
((SELECT id FROM roles WHERE name = 'Admin'), 'CONFIGURE_RNR'),
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_USER'),
((SELECT id FROM roles WHERE name = 'Admin'), 'VIEW_REPORT'),
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_REPORT'),
((SELECT id FROM roles WHERE name = 'Admin'), 'SYSTEM_SETTINGS'),
((SELECT id FROM roles WHERE name = 'Admin'), 'MANAGE_REGIMEN_TEMPLATE');




--Insert into user table

INSERT INTO users
(userName, password, facilityId, firstName, lastName, email, verified, active, restrictLogin) VALUES
  ('StoreInCharge', 'TQskzK3iiLfbRVHeM1muvBCiiKriibfl6lh8ipo91hb74G3OvsybvkzpPI4S3KIeWTXAiiwlUU0iiSxWii4wSuS8mokSAieie',
   (SELECT
      id
    FROM facilities
    WHERE code = 'MZ520495'), 'Hassan', 'Matoto', 'Fatima_Doe@openlmis.com', TRUE, TRUE, FALSE),

  ('FacilityInCharge', 'TQskzK3iiLfbRVHeM1muvBCiiKriibfl6lh8ipo91hb74G3OvsybvkzpPI4S3KIeWTXAiiwlUU0iiSxWii4wSuS8mokSAieie',
   (SELECT
      id
    FROM facilities
    WHERE code = 'MZ520495'), 'Rachel', 'Stephen', 'Jane_Doe@openlmis.com', TRUE, TRUE, FALSE),

  ('lmu', 'TQskzK3iiLfbRVHeM1muvBCiiKriibfl6lh8ipo91hb74G3OvsybvkzpPI4S3KIeWTXAiiwlUU0iiSxWii4wSuS8mokSAieie',
   (SELECT
      id
    FROM facilities
    WHERE code = 'MZ520495'), 'Evance', 'Nkya', 'Frank_Doe@openlmis.com', TRUE, TRUE, FALSE),
    ('Admin123', 'TQskzK3iiLfbRVHeM1muvBCiiKriibfl6lh8ipo91hb74G3OvsybvkzpPI4S3KIeWTXAiiwlUU0iiSxWii4wSuS8mokSAieie', (SELECT
      id
    FROM facilities
    WHERE code = 'MZ520495'), 'John', 'Doe', 'John_Doe@openlmis.com', TRUE, TRUE, FALSE);




--Insert supervisory nodes


INSERT INTO supervisory_nodes
(facilityId, name, code, parentId) VALUES
  ((SELECT id FROM facilities WHERE code = 'MZ520495'), 'Mwanza Zone', 'MWAN-SNZ', NULL);




--Insert role assignments

INSERT INTO role_assignments
(userId, roleId, programId, supervisoryNodeId) VALUES
  ((SELECT ID FROM USERS WHERE username = 'StoreInCharge'), (SELECT id FROM roles WHERE name = 'Create & Submit'), (SELECT id FROM programs WHERE code = 'ilshosp'), NULL),
  ((SELECT ID FROM USERS WHERE username = 'lmu'), (SELECT id FROM roles WHERE name = 'Approve Only'), (SELECT id FROM programs WHERE code = 'ilshosp'), (select id from supervisory_nodes where code='MWAN-SNZ')),
  ((SELECT ID FROM USERS WHERE username = 'FacilityInCharge'), (SELECT id FROM roles WHERE name = 'Authorize only'), (SELECT id FROM programs WHERE code = 'ilshosp'), NULL),
  ((SELECT id FROM users WHERE userName = 'Admin123'), (SELECT id FROM roles WHERE name = 'Admin'), (SELECT id FROM programs WHERE code = 'ilshosp'), NULL);