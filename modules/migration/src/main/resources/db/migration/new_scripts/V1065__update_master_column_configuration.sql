DELETE FROM vaccine_logistics_master_columns WHERE name ='totalAdjustedQuantity';

INSERT INTO vaccine_logistics_master_columns
(name, description, label,indicator, displayOrder, mandatory)
VALUES
('totalAdjustedQuantity', 'Adjusted Quantity', 'Adjusted Quantity', 'P', 17, false);


--Additional Column in vaccine_logistics line Items