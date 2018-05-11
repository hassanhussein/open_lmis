
delete from rights where name = 'VIEW_STOCK_STATUS_LOCATION_REPORT';
INSERT INTO rights (name, rightType, description,createdDate,displayorder,displaynamekey) VALUES
 ('VIEW_STOCK_STATUS_LOCATION_REPORT','REPORT','Permission to view stock status by location',NOW(),22,'report.display.name.stock.status.by.location');

delete from rights where name = 'VIEW_STOCK_STATUS_RMNCH_REPORT';
INSERT INTO rights (name, rightType, description,createdDate,displayorder,displaynamekey) VALUES
 ('VIEW_STOCK_STATUS_RMNCH_REPORT','REPORT','Permission to view RMNCH stock status ',NOW(),23,'report.display.name.rmnch.product.stock.status');
