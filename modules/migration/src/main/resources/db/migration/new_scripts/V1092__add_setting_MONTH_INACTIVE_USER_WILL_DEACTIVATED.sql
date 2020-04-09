delete from configuration_settings where key = 'MONTH_INACTIVE_USER_WILL_DEACTIVATED';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('MONTH_INACTIVE_USER_WILL_DEACTIVATED',6,'Number of months the user will be disabled in month',
'Number of months the user will be disabled in month','GENERAL','NUMBER',true);
