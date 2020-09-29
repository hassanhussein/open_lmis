
delete from losses_adjustments_types where name='RECALLED';
delete from losses_adjustments_types where name='QUARANTINED';
delete from losses_adjustments_types where name='UNQUARANTINED';

INSERT INTO losses_adjustments_types (name, description, additive, displayOrder) VALUES
('RECALLED','Recalled'	,false	,10),
('QUARANTINED', 'Quarantined'	,FALSE,	11),
('UNQUARANTINED', 'Unquarantined',	TRUE,	12);