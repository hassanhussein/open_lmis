delete from configuration_settings where key = 'FACILITY_ELMIS_MAPPING';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('FACILITY_ELMIS_MAPPING','FACILITY_ELMIS_MAPPING','Mapping link of facilities between Facilities Systems and eLMIS','Mapping link of facilities between Facilities Systems and eLMIS','GENERAL','TEXT',true);

