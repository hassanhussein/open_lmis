


DROP TABLE IF EXISTS purchase_documents;
DROP TABLE IF EXISTS asn_lots;
DROP TABLE IF EXISTS asn_details;
DROP TABLE IF EXISTS asns;
DROP TABLE IF EXISTS document_types;
DROP TABLE IF EXISTS ports;
DROP TABLE IF EXISTS countries;

-- countries --------
CREATE TABLE countries (ID SERIAL PRIMARY KEY,
code CHARACTER VARYING (250) NOT NULL UNIQUE,
NAME CHARACTER VARYING (250) NOT NULL UNIQUE,
description TEXT,
active BOOLEAN DEFAULT TRUE,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-----------------  ports -----------------
CREATE TABLE ports (ID SERIAL PRIMARY KEY,
code CHARACTER VARYING (250) NOT NULL UNIQUE,
NAME CHARACTER VARYING (250) NOT NULL UNIQUE,
description TEXT,
active BOOLEAN DEFAULT TRUE,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

---- document_types
CREATE TABLE document_types (ID SERIAL PRIMARY KEY,
NAME CHARACTER VARYING (250) NOT NULL UNIQUE,
description TEXT,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

----/*-------------- purchase orders  NOT IMPLEMENTED -----------------------------------------------------
CREATE TABLE purchase_orders (	id SERIAL PRIMARY KEY,
ordernumber VARCHAR(50) NOT NULL UNIQUE,
orderdate TIMESTAMP NOT NULL,
manufacturerid integer NOT NULL,
representativeid VARCHAR(50),
address VARCHAR(100),
notes VARCHAR(1000),
active BOOLEAN,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_order_details (	id SERIAL PRIMARY KEY,
purchaseorderid integer not null,
expecteddate TIMESTAMP,
expectedquantity INTEGER,
productid integer not null,
uom VARCHAR(20),
price NUMERIC(20,2) DEFAULT 0,
taxes NUMERIC(20,2) DEFAULT 0,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE purchase_order_details ADD CONSTRAINT purchase_order_details_purchase_orders_fkey FOREIGN KEY (purchaseorderid) REFERENCES purchase_orders (id);

ALTER TABLE purchase_order_details ADD CONSTRAINT purchase_order_line_items_products_fkey FOREIGN KEY (productid) REFERENCES products (id);

----*/
---------- asns ------------
CREATE TABLE asns (ID SERIAL PRIMARY KEY,
purchaseorderid INTEGER,
ponumber VARCHAR (255),
podate TIMESTAMP,
supplierid INTEGER,
asnnumber VARCHAR (255),
asndate TIMESTAMP,
blawbnumber VARCHAR (255),
flightvesselnumber VARCHAR (255),
portofarrival INTEGER,
expectedarrivaldate TIMESTAMP,
clearingagent TEXT,status VARCHAR (20),
note TEXT,createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--ALTER TABLE asns ADD CONSTRAINT asns_purchase_orders_fkey FOREIGN KEY (purchaseorderid) REFERENCES purchase_orders (ID);

ALTER TABLE asns ADD CONSTRAINT asns_manufacturers_fkey FOREIGN KEY (supplierid) REFERENCES manufacturers (ID);

---------- asn_details ------------
CREATE TABLE asn_details (ID SERIAL PRIMARY KEY,
asnid INTEGER,
productid INTEGER,
expirydate TIMESTAMP,
manufacturingdate TIMESTAMP,
quantityexpected INTEGER,
lotflag BOOLEAN,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
