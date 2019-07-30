delete from configuration_settings where key = 'NEXLEAF_SSO_PRIVATE_KEY';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('NEXLEAF_SSO_PRIVATE_KEY','','Nexleaf JWT private key','Nexleaf JWT Private Key','Nexleaf','TEXT', true);


delete from configuration_settings where key = 'NEXLEAF_SSO_PUBLIC_KEY';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('NEXLEAF_SSO_PUBLIC_KEY','','Nexleaf JWT public key','Nexleaf JWT Private Key','Nexleaf','TEXT', true);