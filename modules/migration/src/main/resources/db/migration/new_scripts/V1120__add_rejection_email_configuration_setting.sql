delete from configuration_settings where key = 'EMAIL_TEMPLATE_REJECTION';

insert into configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
	values ('EMAIL_TEMPLATE_REJECTION', 'Requisition Rejection Notification email template','Email Notificaiton','This template is used when sending email notification after being rejected.<br />Please use the following as place holders. {receiver_name},
	{facility_name}, {period}, {program_name}','Dear {receiver_name} This is to inform you that requisition for {facility_name}, {program_name} and the Period {period} has been rejected with the following reasons:  {reasons}  <br/>.
	Please login  to <br/> {link}. Thank you.', 'TEXT_AREA', 20);
