DELETE from configuration_settings where key ='DHIS2_USERNAME';

INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('DHIS2_USERNAME','elmis','Username to Access DHIS2 Interface','GENERAL','TEXT');

DELETE from configuration_settings where key ='DHIS2_PASSWORD';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('DHIS2_PASSWORD','Elmis@2020','Password to Access dhis2 Interface','GENERAL','TEXT');

DELETE from configuration_settings where key ='DHIS2_URL';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('DHIS2_URL','https://hisptz.org/dhis2integerations/api/dataValueSets?orgUnitIdScheme=code','URL of dhis2 Interface','GENERAL','TEXT');