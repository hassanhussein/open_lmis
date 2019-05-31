DELETE from configuration_settings where key ='IL_BUDGET_URL';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('IL_BUDGET_URL','https://him-dev.moh.go.tz/rest.api/post/JSON/muungano-gateway-ffars-elmis-response','HIM URL to send Budget Data to FFARS ','GENERAL','TEXT');


DELETE from configuration_settings where key ='IL_BUDGET_USERNAME';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('IL_BUDGET_USERNAME','elmis','HIM Username to send Budget Data to FFARS','GENERAL','TEXT');

DELETE from configuration_settings where key ='IL_BUDGET_PASSWORD';
INSERT INTO configuration_settings(key, value, name, groupname, valuetype)
values('IL_BUDGET_PASSWORD','HIM>?!!>','HIM Password to send Budget Data to FFARS','GENERAL','TEXT');