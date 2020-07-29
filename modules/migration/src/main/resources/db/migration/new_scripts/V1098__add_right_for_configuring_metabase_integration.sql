delete from rights where name = 'CONFIGURE_METABASE_INTEGRATION';
INSERT INTO rights (name, rightType,displaynamekey, description) VALUES
 ('CONFIGURE_METABASE_INTEGRATION','ADMIN','right.configure.metabase.integration','Permission to Configure Metabase Integration');