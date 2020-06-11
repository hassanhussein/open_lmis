

delete from rights where name = 'WMS_APPROVE_DISTRIBUTION';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_APPROVE_DISTRIBUTION','REQUISITION','right.wms.approve.distribution', 39,'Permission to approve distribution');

delete from rights where name = 'WMS_AMMEND_DISTRIBUTION';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('WMS_AMMEND_DISTRIBUTION','REQUISITION','right.wms.ammend.distribution', 40,'Permission to ammend distribution');