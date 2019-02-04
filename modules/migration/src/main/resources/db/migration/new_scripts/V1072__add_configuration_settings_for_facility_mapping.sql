delete from configuration_settings where key = 'FACILITY_ELMIS_MAPPING';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('FACILITY_ELMIS_MAPPING','FACILITY_ELMIS_MAPPING','Mapping link of facilities between Facilities Systems and eLMIS','Mapping link of facilities between Facilities Systems and eLMIS','GENERAL','TEXT',true);


delete from configuration_settings where key = 'FACILITY_TYPE_ELMIS_MAPPING';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('FACILITY_TYPE_ELMIS_MAPPING','FACILITY_TYPE_ELMIS_MAPPING','Mapping link of facilities between FacilitY types Systems and eLMIS','Mapping link of facilities between Facility types Systems and eLMIS','GENERAL','TEXT',true);

delete from configuration_settings where key = 'FACILITY_OWNER_ELMIS_MAPPING';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('FACILITY_OWNER_ELMIS_MAPPING','FACILITY_OWNER_ELMIS_MAPPING','Mapping link of facilities between Facility owner Systems and eLMIS','Mapping link of facilities between Facility owners Systems and eLMIS','GENERAL','TEXT',true);

