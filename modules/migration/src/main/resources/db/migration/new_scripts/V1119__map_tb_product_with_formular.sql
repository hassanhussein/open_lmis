--RHZE
update  products set patientcalculationformula='ADULT_NEW_INTENSIVE_PHASE'
where  code='10010087AE';

--RH 150/75
update  products set patientcalculationformula='ADULT_NEW_CONTINUATION_PHASE'
where   code='10010086AE';


--MB(Adult)
update  products set patientcalculationformula='MB_ADULT'
where  code='10010110AE';

--MB(Pedriatics)
update  products set patientcalculationformula='MB_PEDIATRIC'
where  code='10010113AE';

--PB(Adult)
update  products set patientcalculationformula='PB_ADULT'
where  code='10010112AE';

--PB(Pedriatics)
update  products set patientcalculationformula='PB_PEDIATRIC'
where  code='10010310AE';


--RHZ 75/50/150
update  products set patientcalculationformula='CHILD_NEW_INTENSIVE_PHASE'
where  code='10010256AE';

--Ethambutol 100mg
update  products set patientcalculationformula='CHILD_NEW_INTENSIVE_PHASE'
where  code='10020027AE';


--RH75/50
update  products set patientcalculationformula='CHILD_NEW_CONTINUATION_PHASE'
where  code='10010255AE';

--Isonizid 100mg
update  products set patientcalculationformula='IPT_PEDIATRIC'
where  code='10010114AE';

--Isonizid 300mg
update  products set patientcalculationformula='IPT_ADULT'
where code='10010089AE';

