
delete from rights where name = 'VIEW_REDESIGNED_STOCK_STATUS_REPORT';
INSERT INTO rights (name, righttype, description, displaynamekey) VALUES
('VIEW_REDESIGNED_STOCK_STATUS_REPORT', 'REPORT', 'Permission to View Stock Stock Status Report', 'right.report.redesigned.stock.status');

UPDATE rights SET displayOrder = 62, displayNameKey = 'right.report.redesigned.stock.status' WHERE name = 'VIEW_REDESIGNED_STOCK_STATUS_REPORT';

