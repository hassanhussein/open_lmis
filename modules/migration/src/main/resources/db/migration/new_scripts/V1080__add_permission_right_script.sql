DELETE FROM RIGHTS WHERE name = 'MANAGE_NATIONAL_RECEIVE';

INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
 ('MANAGE_NATIONAL_RECEIVE','REQUISITION','right.manage.national.receive', 63,'Permission to receive vaccines at national level');