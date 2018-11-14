DROP TABLE IF EXISTS elmis_response_messages;
CREATE TABLE elmis_response_messages
(
  id serial,
  code character varying (50),
  description character varying (200),
  usage character varying(200),
  displayorder integer,
  CONSTRAINT elmis_response_messages_pk PRIMARY KEY (id)
);


/*add response seed */

DELETE FROM elmis_response_messages;

INSERT INTO public.elmis_response_messages(code, description,usage,displayorder)
    VALUES ('7000', 'Success', 'Reported when Import is successful',1),
           ('7001','Payload malformed','Error raised when the payload received does not conform to the agreed format',2) ,
           ('7002','Missing value','Error raised when a particular field supposed to have a value has no value associated with it',3),
           ('7003','Entity Not Matched in eLMIS','Error raised when the value of the corresponding fields cannot be matched in eLMIS',4),
           ('7004','Period not in reporting schedule','Error raised when GOTHOMIS_EMR sends an invalid period value',5),
           ('7005','Submission date must be ahead of the reporting/order month','Error raised when GOTHOMIS_EMR sends a payload with this',6),
           ('7006','Value must be greater than zero','Error raised when field value is negative',7),
           ('7007','stockInHand must equal the sum of beginningBalance, quantityReceived and totalAdjustment','Error raised when stockInHand differs from beginningBalance
           + quantityReceived + totalAdjustment - quantityDispensed',8);
