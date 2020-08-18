delete from rights where name = 'TRANSFER_QUARANTINE_LOCATION';
INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
('TRANSFER_QUARANTINE_LOCATION','REQUISITION','right.transfer.quarantine.locations', 48,'Permission to Transfer Quarantine Locations');

