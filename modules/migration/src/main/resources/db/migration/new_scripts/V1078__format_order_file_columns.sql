--
-- This program is part of the OpenLMIS logistics management information system platform software.
-- Copyright © 2013 VillageReach
--
-- This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
--  
-- This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
-- You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
--

DELETE  FROM order_file_columns;

INSERT INTO order_file_columns
(position, openLmisField, dataFieldLabel, nested, keyPath,includeinorderfile, columnLabel, format) VALUES
(1, TRUE, 'header.order.number', 'order', 'id',TRUE,  'Order number', ''),
(2, TRUE, 'create.facility.code', 'order', 'rnr/facility/code',TRUE, 'Facility code', ''),
(3, TRUE, 'header.product.code', 'lineItem', 'productCode',TRUE, 'Product code', ''),
(4, TRUE, 'header.quantity.approved', 'lineItem', 'quantityApproved',TRUE,'Approved quantity', ''),
(5, TRUE, 'label.period', 'order', 'rnr/period/startDate',TRUE, 'Period', 'MM/yy'),
(6, TRUE, 'header.order.date', 'order', 'createdDate',TRUE,'Order date', 'dd/MM/yy'),
(7, TRUE, 'header.product.name', 'lineItem', 'product',TRUE, 'Product name', '');
