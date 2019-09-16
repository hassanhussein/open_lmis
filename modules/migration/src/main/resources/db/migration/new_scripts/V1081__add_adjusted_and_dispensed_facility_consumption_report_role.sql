delete from rights where name = 'VIEW_ADJSUTED_CONSUMPTION';
delete from rights where name = 'VIEW_DISPENSED_CONSUMPTION';


INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_ADJSUTED_CONSUMPTION','REPORT','Permission to view Adjusted Facility Consumption Report');


INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_DISPENSED_CONSUMPTION','REPORT','Permission to view Dispensed Facility Consumption Report');