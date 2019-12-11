DELETE FROM Rights where name='VIEW_AUDIT_TRAIL_REPORT';
INSERT INTO Rights values('VIEW_AUDIT_TRAIL_REPORT', 'REPORT', 'Permission to audit trail', NOW(), null, 'right.view.audit.trail');