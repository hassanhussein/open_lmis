
delete from configuration_settings where key = 'GENERAL_INFO';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('GENERAL_INFO',
  'General Information','Dashboard','','To be added','TEXT_AREA', 42);