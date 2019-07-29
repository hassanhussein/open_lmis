DELETE FROM RIGHTS WHERE name = 'VIEW_RTM_DASHBOARD';

INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
 ('VIEW_RTM_DASHBOARD','REPORT','right.view.rtm.dashboard', 63,'Permission to view rtm dashboard');