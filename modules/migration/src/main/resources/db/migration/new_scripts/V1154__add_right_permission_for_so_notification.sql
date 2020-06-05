
DELETE FROM role_rights WHERE rightname = 'VIEW_STOCK_OUT_NOTIFICATION_MENU';
DELETE FROM rights WHERE name = 'VIEW_STOCK_OUT_NOTIFICATION_MENU';

INSERT INTO rights (name, rightType, displaynamekey, description, displayOrder) VALUES
  ('VIEW_STOCK_OUT_NOTIFICATION_MENU','REPORT','right.manage.stock.out.notification.menu','Permission to view Stock out notification menu', 37);