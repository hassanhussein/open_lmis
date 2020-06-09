delete from configuration_settings where key = 'DB_DASHLETS_EMERGECNCY_ORDER_ANALYSIS';

insert into configuration_settings(key, value, name, description, groupname, valuetype, isconfigurable)
values('DB_DASHLETS_EMERGECNCY_ORDER_ANALYSIS','http://insights2.zm-elmis.org/public/dashboard/11c4e784-eb24-4f0a-81fa-f78deedf8f75',
'Emergency Order Analysis','Emergency Order Analysis dashlet','Main Dashboard','TEXT',true);