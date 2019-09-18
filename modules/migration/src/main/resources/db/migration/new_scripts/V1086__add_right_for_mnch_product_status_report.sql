delete from rights where name = 'VIEW_RMNCH_PRODUCT_STOCK_STATUS';
INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_RMNCH_PRODUCT_STOCK_STATUS','REPORT','Permission to view RMNCH Product Stock Status report');

update rights set displayNameKey = 'label.rights.view.RMNCH.product.stock.status.report' where name = 'VIEW_RMNCH_PRODUCT_STOCK_STATUS';