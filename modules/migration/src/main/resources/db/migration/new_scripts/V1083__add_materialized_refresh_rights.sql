delete from rights where name = 'REFRESH_MATERIALIZED_VIEW';
INSERT INTO rights (name, rightType, description) VALUES
 ('REFRESH_MATERIALIZED_VIEW','ADMIN','Permission to refresh materialized views');

update rights set displayNameKey = 'label.rights.refresh.materialized.view.admin' where name = 'REFRESH_MATERIALIZED_VIEW';