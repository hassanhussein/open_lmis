delete from configuration_settings where key = 'DASHLET_RNR_STATUS_SUMMARY';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_RNR_STATUS_SUMMARY',
  'Dashlet Requisition Status Summary','Dashboard','',' Definition to be Added','TEXT_AREA', 42);
