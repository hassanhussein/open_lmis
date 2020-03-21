delete from configuration_settings where key = 'GOTHOMIS_USERNAME';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('GOTHOMIS_USERNAME','gothomis-client','Username for GoTHOMIS Instance','Username for GoTHOMIS Instance','GENERAL','TEXT',true);

delete from configuration_settings where key = 'GOTHOMIS_PASSWORD';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('GOTHOMIS_PASSWORD','123456','Password for GoTHOMIS Instance','Password for GoTHOMIS Instance','GENERAL','TEXT',true);

delete from configuration_settings where key = 'GOTHOMIS_URL';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('GOTHOMIS_URL','http://41.59.1.94:50003/gothomis-authentication-service/oauth/token','URL for GoTHOMIS Instance','URL for GoTHOMIS Instance','GENERAL','TEXT',true);


delete from configuration_settings where key = 'GRANT_TYPE';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('GRANT_TYPE','password','GRANT TYPE for GoTHOMIS Instance','GRant Type for GoTHOMIS Instance','GENERAL','TEXT',true);

delete from configuration_settings where key = 'URL_POST_GOTHMIS';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('URL_POST_GOTHMIS','http://41.59.1.94:50003/gothomis-rnr-service/gothomis/rnr/status/tracking','URL to Post GOTHOMIS','URL to Post GOTHOMIS','GENERAL','TEXT',true);

delete from configuration_settings where key = 'TOKEN_ACCESS_USERNAME';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('TOKEN_ACCESS_USERNAME','matoto@gmail.com','Token Access username','Token Access username','GENERAL','TEXT',true);

delete from configuration_settings where key = 'TOKEN_ACCESS_PASSWORD';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('TOKEN_ACCESS_PASSWORD','12345678','Token Access password','Token Access password','GENERAL','TEXT',true);

