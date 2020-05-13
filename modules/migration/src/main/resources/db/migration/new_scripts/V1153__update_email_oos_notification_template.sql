
ALTER TABLE email_attachments ALTER COLUMN attachmentpath DROP NOT NULL;

    DO $$
    BEGIN
        BEGIN
            ALTER TABLE email_attachments ADD COLUMN fileSource bytea default null;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column fileSource already exists in email_attachments.';
        END;
    END;
$$;


DELETE FROM configuration_settings where key = 'OUT_OF_STOCK_EMAIL_MESSAGE_TEMPLATE';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('OUT_OF_STOCK_EMAIL_MESSAGE_TEMPLATE', 'Stock Out Notification Email Notification Template','Notification','',E'Dear {name} \n\nPlease receive OOS for {facility_name} facility reported on {period} period for {program}.
   \n\nThank you', 'TEXT_AREA', 30);


DELETE FROM configuration_settings where key = 'OUT_OF_STOCK_EMAIL_SUBJECT_TEMPLATE';
insert into configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
values ('OUT_OF_STOCK_EMAIL_SUBJECT_TEMPLATE', 'Stock Out Notification SUBJECT Email Template','Notification - Email','This is the subject message for Out of stock Notification','Out Of Stock Notification', 'TEXT_AREA', 2);



