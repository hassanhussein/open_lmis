
delete from configuration_settings where key = 'UNDER_STOCKED';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('UNDER_STOCKED','#FFFF00','Blue color for Under Stocked Color','Graph color when the product is under stocked','GENERAL','TEXT',true);

delete from configuration_settings where key = 'ADEQUATELY_STOCKED';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('ADEQUATELY_STOCKED','#009F48','Green color for Adequately Stocked','Graph color when stock is Adequately','GENERAL','TEXT',true);

delete from configuration_settings where key = 'OVER_STOCKED';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('OVER_STOCKED','#00AFEE','Yellow color for Over Stocked','Graph color when is Overstocked','GENERAL','TEXT',true);

delete from configuration_settings where key = 'STOCKED_OUT';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('STOCKED_OUT','#FE0000','Stocked Out','Graph color when there is no Stock','GENERAL','TEXT',true);

delete from configuration_settings where key = 'UNKNOWN_STOCK';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('UNKNOWN_STOCK','#E3E3E3','Black color for UNKNOWN','Graph color when is Unknown','GENERAL','TEXT',true);

delete from configuration_settings where key = 'GOTHOMIS-ELMIS-INTERFACE';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('GOTHOMIS-ELMIS-INTERFACE','GOTHOMIS-ELMIS-INTERFACE','Mapping link of facilities between GotHomis and eLMIS','Mapping link of facilities between GotHomis and eLMIS','GENERAL','TEXT',true);



/*Add stock status Color function
*/

DROP FUNCTION IF EXISTS public.fn_get_stock_color(character varying);

CREATE OR REPLACE FUNCTION public.fn_get_stock_color(in_status_code character varying)
  RETURNS character varying AS
$BODY$
DECLARE
color varchar(7);
t_color varchar(7);
BEGIN
color='#E3E3E3';
IF in_status_code = 'US' THEN
t_color=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('UNDER_STOCKED') LIMIT 1);
IF t_color IS NOT NULL AND t_color <> '' THEN
color=t_color;
END IF;
ELSIF in_status_code = 'SP'  THEN
t_color=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('ADEQUATELY_STOCKED') LIMIT 1);
IF t_color IS NOT NULL AND t_color <> '' THEN
color=t_color;
END IF;

ELSIF in_status_code = 'OS'  THEN
t_color=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('OVER_STOCKED') LIMIT 1);
IF t_color IS NOT NULL AND t_color <> '' THEN
color=t_color;
END IF;

ELSIF in_status_code = 'SO'  THEN
t_color=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('STOCKED_OUT') LIMIT 1);
IF t_color IS NOT NULL AND t_color <> '' THEN
color=t_color;
END IF;

ELSE
t_color=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('UNKNOWN_STOCK') LIMIT 1);
IF t_color IS NOT NULL AND t_color <> '' THEN
color=t_color;
END IF;
END IF;
return color;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.fn_get_stock_color(character varying)
  OWNER TO postgres;





/*Stock Status Configurations */

delete from configuration_settings where key = 'RELEASED';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('RELEASED','This Order has been Released to MSD Epicor 9','This Order has been Released to MSD Epicor 9','Order has been Released to MSD Epicor 9','GENERAL','TEXT',true);

delete from configuration_settings where key = 'APPROVED';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('APPROVED','This Requisition Is at MSD for Conversion to Order','This Requisition Is at MSD for Conversion to Order','This Requisition Is at MSD for Conversion to Order','GENERAL','TEXT',true);

delete from configuration_settings where key = 'IN_APPROVAL';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('IN_APPROVAL','This requisition is at Logistics Management Unit for REVIEW and APPROVAL','This requisition is at Logistics Management Unit for REVIEW and APPROVAL','This requisition is at Logistics Management Unit for REVIEW and APPROVAL','GENERAL','TEXT',true);

delete from configuration_settings where key = 'AUTHORIZED';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('AUTHORIZED','This requisition is at the District for REVIEW and APPROVAL','This requisittion is at the District for REVIEW and APPROVAL','This requisittion is at the District for REVIEW and APPROVAL','GENERAL','TEXT',true);

delete from configuration_settings where key = 'UNKNOWN_STATUS';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('UNKNOWN_STATUS','No Requisition Currently','No Requisition Currently','No Requisition Currently','GENERAL','TEXT',true);


DROP FUNCTION if exists fn_get_rnr_status_descriptions(character varying);

CREATE OR REPLACE FUNCTION public.fn_get_rnr_status_descriptions(in_status_code character varying)
  RETURNS character varying AS
$BODY$
DECLARE
status varchar(300);
t_status varchar(300);
BEGIN
status='No Requisition Currently';
IF in_status_code = 'RELEASED' THEN
t_status=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('RELEASED') LIMIT 1);
IF t_status IS NOT NULL AND t_status <> '' THEN
status=t_status;
END IF;
ELSIF in_status_code = 'APPROVED'  THEN
t_status=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('APPROVED') LIMIT 1);
IF t_status IS NOT NULL AND t_status <> '' THEN
status=t_status;
END IF;

ELSIF in_status_code = 'IN_APPROVAL'  THEN
t_status=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('IN_APPROVAL') LIMIT 1);
IF t_status IS NOT NULL AND t_status <> '' THEN
status=t_status;
END IF;

ELSIF in_status_code = 'AUTHORIZED'  THEN
t_status=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('AUTHORIZED') LIMIT 1);
IF t_status IS NOT NULL AND t_status <> '' THEN
status=t_status;
END IF;

ELSE
t_status=(SELECT value FROM configuration_settings WHERE LOWER(key) = LOWER('UNKNOWN_STATUS') LIMIT 1);
IF t_status IS NOT NULL AND t_status <> '' THEN
status=t_status;
END IF;
END IF;
return status;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.fn_get_rnr_status_descriptions(character varying)
  OWNER TO postgres;
