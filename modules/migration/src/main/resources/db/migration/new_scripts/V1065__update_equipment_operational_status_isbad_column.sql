update equipment_operational_status set isbad = true, isObsolete = false where code = 'NON_OPERATONAL';
update equipment_operational_status set isbad = false, isObsolete = false where code = 'FUNCTIONAL';
update equipment_operational_status set isbad = true, isObsolete = true where code = 'OBSOLETE';