delete from rights where name = 'VIEW_REPORTING_STATUS_FACILITIES';
INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_REPORTING_STATUS_FACILITIES','REPORT','Permission to view reporting status of facilities report');

update rights set displayNameKey = 'label.rights.view.reporting.facility.status.report' where name = 'VIEW_REPORTING_STATUS_FACILITIES';