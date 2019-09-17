delete from rights where name = 'VIEW_REJECTED_RNRS_REPORT';
INSERT INTO rights (name, rightType, description) VALUES
 ('VIEW_REJECTED_RNRS_REPORT','REPORT','Permission to view Rejected Rnrs report');

update rights set displayNameKey = 'label.rights.view.rejected.rnrs.report' where name = 'VIEW_REJECTED_RNRS_REPORT';