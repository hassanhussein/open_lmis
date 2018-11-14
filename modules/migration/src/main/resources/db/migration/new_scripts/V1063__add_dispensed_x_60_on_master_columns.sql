UPDATE master_rnr_columns
set calculationOption =
'[{"name":"Default", "id":"DEFAULT"},
{"name":"Dispensed Quantity + No of New Patients","id":"DISPENSED_PLUS_NEW_PATIENTS"},
{"name":"(Dispensed x 90) / (90 - Stockout Days)","id":"DISPENSED_X_90"},
{"name":"(Dispensed x 60) / (60 - Stockout Days)","id":"DISPENSED_X_60"}
]'
where name = 'normalizedConsumption';