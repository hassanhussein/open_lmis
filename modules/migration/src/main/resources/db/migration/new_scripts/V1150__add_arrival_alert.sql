
DROP TABLE IF EXISTS alert_asn_expected_arrival;
CREATE TABLE public.alert_asn_expected_arrival
(
  ponumber character varying(255),
  supplierName character varying(255),
  podate timestamp without time zone,
  expectedarrivaldate timestamp without time zone,
  clearingagent text,
  portofarrival character varying(255),
  asnId INTEGER
);




delete from configuration_settings where key = 'EXEPECTED_DAYS_CONSIGNMENT_TO_ARRIVE';
insert into configuration_settings(key,value,name,description,groupname,valuetype,isconfigurable)
values('EXEPECTED_DAYS_CONSIGNMENT_TO_ARRIVE',20,'Pre-advice: Expected Days to send notification Consignment to be arrived','Pre-advice: Expected Days to send notification Consignment to be arrived','VACCINE','TEXT',true);





DROP FUNCTION IF EXISTS public.fn_populate_alert_asn_expected_arrival(integer);

CREATE OR REPLACE FUNCTION public.fn_populate_alert_asn_expected_arrival(in_flag integer)
  RETURNS character varying AS
$BODY$
DECLARE
rec_detail RECORD ;
msg CHARACTER VARYING (2000) ;
BEGIN

msg := 'Success!!! fn_populate_equipment_nonfunctional - Data saved successfully' ;
DELETE FROM alert_asn_expected_arrival;

FOR rec_detail IN

select ponumber, podate,s.name supplierName,expectedarrivaldate ,EXTRACT (day from expectedarrivaldate) days,
clearingagent, ports.name portofarrival, a.id asnId
from ASNs a JOIN supply_partners s ON s.id = a.supplierid
JOIN ports ON ports.id = a.portofarrival
where EXTRACT (day from expectedarrivaldate) < (SELECT value::int from configuration_settings where key='EXEPECTED_DAYS_CONSIGNMENT_TO_ARRIVE')

and (EXTRACT(day from expectedarrivaldate) > (extract (DAY from CURRENT_DATE)))

LOOP

INSERT INTO alert_asn_expected_arrival(ponumber, supplierName, podate,expectedarrivaldate, clearingagent, portofarrival, asnId )
VALUES (rec_detail.ponumber, rec_detail.supplierName, rec_detail.podate, rec_detail.expectedarrivaldate::Date,rec_detail.clearingagent,rec_detail.portofarrival,rec_detail.asnId );

END LOOP;

RETURN msg ;
EXCEPTION
WHEN OTHERS THEN
RETURN 'Error!!! fn_populate_alert_asn_expected_arrival.' || SQLERRM ;
END ;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.fn_populate_alert_asn_expected_arrival(integer)
  OWNER TO postgres;

DELETE FROM configuration_settings WHERE KEY='EMAIL_TEMPLATE_EXPECTED_ARRIVAL';
DELETE FROM configuration_settings WHERE KEY='EMAIL_SUBJECT_EXPECTED_ARRIVAL';
insert into configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
	values ('EMAIL_TEMPLATE_EXPECTED_ARRIVAL', 'Pre-advice: Expected Arrival Notification email template','Email Notificaiton','This template is used when sending email notification to receiver of consignment.
	<br />Please use the following as place holders. {receiver_name}, {expected_arrival_date}, {pon_umber}, {port_of_arrival},{clearing_agent}','Dear {receiver_name} This is to inform you that {po_number}, {expected_arrival_date},{port_of_arrival},{clearing_agent}, {supplier_Name}, {po_date} is expected to be arrived in 3 days to come. Thank you.', 'TEXT_AREA', 10);

insert into configuration_settings (key, name, groupname, description, value, valueType, displayOrder)
	values ('EMAIL_SUBJECT_EXPECTED_ARRIVAL', 'Pre-advice: Expected Arrival Notification email template Subject','Email Notificaiton', null ,'Your Action is Required', 'TEXT',10);