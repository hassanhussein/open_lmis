DELETE from configuration_settings where key ='LLIN_DHIS_SECOND_URL';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('LLIN_DHIS_SECOND_URL','','DHIS URL2 to send LLIN DATA ','GENERAL','TEXT');