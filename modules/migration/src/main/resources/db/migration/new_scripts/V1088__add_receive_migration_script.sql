ALTER TABLE purchase_documents DROP CONSTRAINT IF EXISTS purchase_documents_receives_fkey;
ALTER TABLE purchase_documents DROP COLUMN IF EXISTS receiveid;
DROP TABLE IF EXISTS receive_lots;
DROP TABLE IF EXISTS receive_details;
DROP TABLE IF EXISTS receives;



---------- receives ------------
CREATE TABLE receives (ID SERIAL PRIMARY KEY,
purchaseorderid INTEGER,
ponumber VARCHAR (255),
podate TIMESTAMP,
supplierid INTEGER,
asnid INTEGER NULL,
receivedate TIMESTAMP,
blawbnumber VARCHAR (255),
country VARCHAR (255),
flightvesselnumber VARCHAR (255),
portofarrival INTEGER,
expectedarrivaldate TIMESTAMP,
actualarrivaldate TIMESTAMP,
clearingagent TEXT,
shippingagent TEXT,
status VARCHAR (20),
note TEXT,
notetosupplier TEXT,
description TEXT,
isforeignprocument BOOLEAN,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--ALTER TABLE asns ADD CONSTRAINT asns_purchase_orders_fkey FOREIGN KEY (purchaseorderid) REFERENCES purchase_orders (ID);

ALTER TABLE receives ADD CONSTRAINT receives_supply_partners_fkey FOREIGN KEY (supplierid) REFERENCES supply_partners (ID);

ALTER TABLE receives ADD CONSTRAINT receives_asns_fkey FOREIGN KEY (asnid) REFERENCES asns (ID);



---------- receive_details ------------
CREATE TABLE receive_details (ID SERIAL PRIMARY KEY,
receiveid INTEGER,
productid INTEGER,
expirydate TIMESTAMP,
manufacturingdate TIMESTAMP,
quantitycounted INTEGER,
unitprice NUMERIC(20,2),
boxcounted INTEGER,
lotflag BOOLEAN,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE receive_details ADD CONSTRAINT receive_details_receives_fkey FOREIGN KEY (receiveid) REFERENCES receives (ID);

ALTER TABLE receive_details ADD CONSTRAINT receive_details_products_fkey FOREIGN KEY (productid) REFERENCES products (ID);


---------- receive_lots ------------
CREATE TABLE receive_lots (ID SERIAL PRIMARY KEY,
receivedetailid INTEGER,
locationid INTEGER,
lotnumber VARCHAR (255),
serialnumber VARCHAR (255),
expirydate TIMESTAMP,
manufacturingdate TIMESTAMP,
quantity INTEGER,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE receive_lots ADD CONSTRAINT receive_lots_receive_details_fkey FOREIGN KEY (receivedetailid) REFERENCES receive_details (ID);
ALTER TABLE receive_lots ADD CONSTRAINT receive_lots_locations_fkey FOREIGN KEY (locationid) REFERENCES locations (ID);


----------alter purchase_documents ------------

ALTER TABLE purchase_documents ADD COLUMN receiveid INTEGER NULL;

ALTER TABLE purchase_documents ADD CONSTRAINT purchase_documents_receives_fkey FOREIGN KEY (receiveid) REFERENCES receives (ID);