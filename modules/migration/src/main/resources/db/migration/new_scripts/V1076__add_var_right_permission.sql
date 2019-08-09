DELETE FROM RIGHTS WHERE name = 'MANAGE_VAR';

INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
 ('MANAGE_VAR','REQUISITION','right.manage.var', 62,'Permission to view vaccine arrival report');