

update order_file_columns set position=1
where dataFieldLabel='header.order.number'
;

update order_file_columns set position=2
where dataFieldLabel='create.facility.code'
;
update order_file_columns set position=3
where dataFieldLabel='header.product.code'
;
update order_file_columns set position=4
where dataFieldLabel='header.quantity.approved'
;

update order_file_columns set position=5
where dataFieldLabel='label.period'
;

update order_file_columns set position=6
where dataFieldLabel='header.order.date'
;

update order_file_columns set position=7
where dataFieldLabel='header.product.name'
;
