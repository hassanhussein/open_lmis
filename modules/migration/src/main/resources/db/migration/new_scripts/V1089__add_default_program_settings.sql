delete from configuration_settings where key = 'DEFAULT_PROGRAM_CODE';

INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DEFAULT_PROGRAM_CODE', 'Default Program Code', 'Dashboard', '','ILS',  'TEXT', 1);