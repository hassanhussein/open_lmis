DELETE FROM configuration_settings where key = 'EMAIL_TEMPLATE_FOR_REJECTED_REQUISITION';
insert into  configuration_settings
(key, name, groupname, description, value, valueType,displayOrder)
values
('EMAIL_TEMPLATE_FOR_REJECTED_REQUISITION',
'Email Template for Rejected Requisition',
'Notification - Email',
'Email Template for Rejected Requisition',
'<h2><strong>Your requisition has been rejected.</strong></h2><p></p><p>Dear ${model.name},</p><p><br></p><p>Facility ${model.facility} had a requisition submitted for ${model.program} program, ${model.period}. However, an approver has rejected it. please <a href="${model.url}">click here</a> to open the requisition.</p><p><br></p><p>Thanks,<br></p>', 'HTML', 57);

DELETE FROM configuration_settings where key = 'EMAIL_SUBJECT_FOR_REJECTED_REQUISITION';
insert into  configuration_settings
(key, name, groupname, description, value, valueType,displayOrder)
values
('EMAIL_SUBJECT_FOR_REJECTED_REQUISITION',
'Email Subject for Rejected Requisition',
'Notification - Email',
'Email Subject for Rejected Requisition',
'A requisition you acted on has been rejected', 'TEXT', 56);
