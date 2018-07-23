ALTER TABLE master_rnr_columns ALTER COLUMN calculationOption TYPE varchar(300);

UPDATE master_rnr_columns
set calculationOption = '[{"name":"Default", "id":"DEFAULT"},{"name":"Normalized Consumption x 2","id":"CONSUMPTION_X_2"},{"name":"Dispensed Quantity x 2","id":"DISPENSED_X_2"},{"name":"Normalized Consumption x 3","id":"CONSUMPTION_X_3"}]'
where name = 'maxStockQuantity';