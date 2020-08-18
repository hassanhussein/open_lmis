
DELETE FROM role_rights WHERE RIGHTNAME ='WMS_CREATE_ASN';
DELETE FROM role_rights WHERE RIGHTNAME ='WMS_RECEIVE_CONSIGNMENT';
DELETE FROM role_rights WHERE RIGHTNAME ='WMS_INSPECT_CONSIGNMENT';
DELETE FROM role_rights WHERE RIGHTNAME ='WMS_PUT_AWAY';
DELETE FROM role_rights WHERE RIGHTNAME ='WMS_MANAGE_STOCK';
DELETE FROM role_rights WHERE RIGHTNAME ='WMS_MANAGE_ORDERS';
DELETE FROM role_rights WHERE RIGHTNAME ='WMS_MANAGE_WAREHOUSE';

delete from rights where name = 'WMS_CREATE_ASN';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_CREATE_ASN','REQUISITION','right.wms.create.asn', 41,'Permission to Create Pre-advise/Asn');

delete from rights where name = 'WMS_RECEIVE_CONSIGNMENT';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_RECEIVE_CONSIGNMENT','REQUISITION','right.wms.receive.consignment', 42,'Permission to Receive Consignments');

delete from rights where name = 'WMS_INSPECT_CONSIGNMENT';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_INSPECT_CONSIGNMENT','REQUISITION','right.wms.inspection', 43,'Permission to Inspect Consignments');

delete from rights where name = 'WMS_PUT_AWAY';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_PUT_AWAY','REQUISITION','right.wms.put.away', 44,'Permission to manage  put-away');

delete from rights where name = 'WMS_MANAGE_STOCK';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_MANAGE_STOCK','REQUISITION','right.manage.wms.stock', 45,'Permission to manage wms stock');

delete from rights where name = 'WMS_MANAGE_ORDERS';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_MANAGE_ORDERS','REQUISITION','right.manage.order', 46,'Permission to manage wms orders');

delete from rights where name = 'WMS_MANAGE_WAREHOUSE';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_MANAGE_WAREHOUSE','REQUISITION','right.manage.warehouse', 47,'Permission to manage Warehouse Management System');

