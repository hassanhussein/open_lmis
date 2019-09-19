DELETE FROM RIGHTS WHERE name = 'RECEIVE_FINALIZED_ASN_REPORT';

INSERT INTO rights (name, rightType, displaynamekey, displayOrder, description) VALUES
 ('RECEIVE_FINALIZED_ASN_REPORT','REPORT','right.finalize.asn', 63,'Permission to receive ASN arrival report');


delete from configuration_settings where key = 'EMAIL_TEMPLATE_FOR_ASN_REPORT';

INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('EMAIL_TEMPLATE_FOR_ASN_REPORT',
  'Email Notification Template for Receiving ASN Finalized Report','Notification','',

E'Dear {user_name}

This is to inform you that ASN Pre-advice has been finalized for the order that was requested on {date_submitted}. Please make follow-up.
{link}
Thank you.','TEXT_AREA', 47);