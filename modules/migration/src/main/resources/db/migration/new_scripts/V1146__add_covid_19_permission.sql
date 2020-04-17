DELETE from role_rights where rightName = 'VIEW_COVID_19_DASHBOARD';
DELETE from rights where name = 'VIEW_COVID_19_DASHBOARD';
INSERT INTO rights (name, rightType, displayNameKey, displayOrder, description) VALUES
('VIEW_COVID_19_DASHBOARD','REPORT','right.view.covid.dashboard', 110,'Permission to view Covid 19 Dashboard');