DELETE FROM RIGHTS WHERE name = 'MANAGE_LOCATION';

INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
 ('MANAGE_LOCATION','REQUISITION','right.manage.location', 63,'Permission to view vaccine arrival report');