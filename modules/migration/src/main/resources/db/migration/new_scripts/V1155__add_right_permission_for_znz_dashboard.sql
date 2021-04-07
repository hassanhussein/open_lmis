DELETE from role_rights where rightName = 'VIEW_ZNZ_DASHBOARD';
DELETE from rights where name = 'VIEW_ZNZ_DASHBOARD';
INSERT INTO rights (name, rightType, displayNameKey, displayOrder, description) VALUES
('VIEW_ZNZ_DASHBOARD','REPORT','right.view.znz.dashboard', 115,'Permission to view Zanzibar Dashboard');