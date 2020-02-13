UPDATE master_rnr_columns
set calculationOption = '[{"name":"Default", "id":"DEFAULT"},{"name":"Normalized Consumption x 2","id":"CONSUMPTION_X_2"},{"name":"Dispensed Quantity x 2","id":"DISPENSED_X_2"},{"name":"Dispensed Quantity x 3","id":"DISPENSED_X_3"},{"name":"Maximum Requirement","id":"MAXIMUM_REQUIREMENT"}]'
where name = 'maxStockQuantity';