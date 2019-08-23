UPDATE order_file_columns
SET position = position - 1
WHERE position > 4;
update order_file_columns set position=8
where dataFieldLabel='header.product.name'
and keyPath='product';
