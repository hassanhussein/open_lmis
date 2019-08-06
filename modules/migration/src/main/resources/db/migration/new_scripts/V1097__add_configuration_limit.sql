DELETE FROM configuration_settings WHERE key = 'DEFAULT_CUSTOM_LIMIT';
INSERT INTO configuration_settings(key, value, name, description, groupname, valuetype)
values('DEFAULT_CUSTOM_LIMIT',35000,'Default Limit to export quantification report','Default Limit to export quantification report.','GENERAL','TEXT');
