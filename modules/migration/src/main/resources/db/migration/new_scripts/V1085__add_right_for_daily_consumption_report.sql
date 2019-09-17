delete from rights where name = 'VIEW_DAILY_CONSUMPTION_REPORT';
INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_DAILY_CONSUMPTION_REPORT','REPORT','Permission to view Daily Consumption report');

update rights set displayNameKey = 'label.rights.view.daily.consumption.report' where name = 'VIEW_DAILY_CONSUMPTION_REPORT';