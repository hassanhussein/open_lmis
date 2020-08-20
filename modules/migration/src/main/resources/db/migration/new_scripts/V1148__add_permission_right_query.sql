delete from rights where name = 'FINALIZE_ASN';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('FINALIZE_ASN','REQUISITION','right.finalize.pre.advice', 49,'Permission to Finalize Asn');

delete from rights where name = 'FINALIZE_RECEIVE';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('FINALIZE_RECEIVE','REQUISITION','right.finalize.receive', 50,'Permission to Finalize Receive');

delete from rights where name = 'FINALIZE_INSPECTION';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('FINALIZE_INSPECTION','REQUISITION','right.finalize.inspection', 51,'Permission to Finalize Inspection');

delete from rights where name = 'SAVE_ASN';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('SAVE_ASN','REQUISITION','right.save.pre.advice', 52,'Permission to Finalize Asn');

delete from rights where name = 'SAVE_RECEIVE';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('SAVE_RECEIVE','REQUISITION','right.save.receive', 53,'Permission to Save Receive');

delete from rights where name = 'SAVE_INSPECTION';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('SAVE_INSPECTION','REQUISITION','right.save.inspection', 54,'Permission to Save Inspection');

