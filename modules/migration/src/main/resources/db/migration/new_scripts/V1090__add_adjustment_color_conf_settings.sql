delete from configuration_settings where key = 'ADJUSTMENT_COLOR_THRESHOLD_LOW';

INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('ADJUSTMENT_COLOR_THRESHOLD_LOW', 'Adjustment Color Threshold Low', 'Dashboard', '','25',  'TEXT', 1);


delete from configuration_settings where key = 'ADJUSTMENT_COLOR_THRESHOLD_MEDIUM';

INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('ADJUSTMENT_COLOR_THRESHOLD_MEDIUM', 'Adjustment Color Threshold Medium', 'Dashboard', '','75',  'TEXT', 1);



delete from configuration_settings where key = 'ADJUSTMENT_COLOR_THRESHOLD_HIGH';

INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('ADJUSTMENT_COLOR_THRESHOLD_HIGH', 'Adjustment Color Threshold High', 'Dashboard', '','100',  'TEXT', 1);