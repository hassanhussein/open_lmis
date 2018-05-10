
delete from rights where name = 'VIEW_STOCK_STATUS_LOCATION_REPORT';
INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_STOCK_STATUS_LOCATION_REPORT','REPORT','Permission to view stock status by location');

delete from rights where name = 'VIEW_STOCK_STATUS_RMNCH_REPORT';
INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_STOCK_STATUS_RMNCH_REPORT','REPORT','Permission to view RMNCH stock status ');
