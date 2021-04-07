update  patient_categories set name='6 Lfx – Bdq – Lzd – Cs AND 3 Lfx – Lzd – Cs (Pediatric)' where code='MDR-REGIMEN-3';

update  program_rnr_columns set calculationoption='LOCK_FACILITY_OPEN_DISTRICT' where programid=(select id from programs where code = 'TB-MDR')
and mastercolumnid=(select id from master_rnr_columns where name='nextMonthPatient');

update  products set patientcalculationformula='CHILD_NEW_INTENSIVE_PHASE'
where  code='10010251AE';