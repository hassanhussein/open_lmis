delete from configuration_settings where key = 'LOCAL_TIME_SETTINGS';

INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('LOCAL_TIME_SETTINGS', 'Time zone for specific country in which eLMIS is running ', 'GENERAL', '','Africa/Nairobi',  'TEXT', 1);