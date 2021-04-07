DELETE FROM rights where name = 'VIEW_OUT_OF_STOCK_NOTIFICATION';
INSERT INTO rights (name, rightType, displayNameKey, displayOrder, description) VALUES
('VIEW_OUT_OF_STOCK_NOTIFICATION','REQUISITION','right.vew.stock.out.notification', 109,'Permission to view out of stock Notification');
