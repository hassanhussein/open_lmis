
DELETE from configuration_settings where key ='HIM_USERNAME';

INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('HIM_USERNAME','EpicorHIM','Username to Access HIM for OOS/DHIS and other Systems','GENERAL','TEXT');

DELETE from configuration_settings where key ='HIM_PASSWORD';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('HIM_PASSWORD','Epicor>!_56','Password to Access HIM for OOS/DHIS','GENERAL','TEXT');

delete from configuration_settings where key = 'OOS_HIM_URL';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('OOS_HIM_URL','https://him-dev.moh.go.tz/rest.api/post/JSON/e9-out-of-stock-notification-response','Out of Stock HIM url','Out of Stock HIM URL','GENERAL','TEXT',true);
