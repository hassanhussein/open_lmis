delete from configuration_settings where key = 'MSD_ELMIS_BUDGET_INTERGRATION_URL';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('MSD_ELMIS_BUDGET_INTERGRATION_URL','https://portal.msd.go.tz/web/index.php/mobile/customer/customer-statement-detailed',
'MSD URL TO POST DATA','Used to pull current customer statement from MSD','GENERAL','TEXT',true);

delete from configuration_settings where key = 'MSD_ELMIS_BUDGET_INTERGRATION_USERNAME';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('MSD_ELMIS_BUDGET_INTERGRATION_USERNAME','','username','Used for authentication','GENERAL','TEXT',true);

delete from configuration_settings where key = 'MSD_ELMIS_BUDGET_INTERGRATION_PASSWORD';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('MSD_ELMIS_BUDGET_INTERGRATION_PASSWORD','','Password','Used for authentication','GENERAL','TEXT',true);
