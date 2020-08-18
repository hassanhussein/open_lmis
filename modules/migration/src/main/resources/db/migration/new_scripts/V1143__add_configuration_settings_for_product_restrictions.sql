delete from configuration_settings where key = 'MAXIMUM_DAYS_PRODUCT_EXPIRY_RESTRICTION';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('MAXIMUM_DAYS_PRODUCT_EXPIRY_RESTRICTION',30,'Maximum Days Product Expiry Limit','Used to determine the days in which products is expected to expire','VACCINE','TEXT',true);
