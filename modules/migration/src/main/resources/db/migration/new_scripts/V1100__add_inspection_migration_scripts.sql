DROP TABLE IF EXISTS inspection_lot_problems;
DROP TABLE IF EXISTS inspection_lots;
DROP TABLE IF EXISTS inspection_line_items;
DROP TABLE IF EXISTS inspections;

---------- inspections ------------
CREATE TABLE inspections (ID SERIAL PRIMARY KEY,
receiveid  INTEGER NOT NULL,
inspectiondate TIMESTAMP,
inspectionnote TEXT,
inspectedby TEXT,
status VARCHAR (20),
createBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE inspections ADD CONSTRAINT inspections_receives_fkey FOREIGN KEY (receiveid) REFERENCES receives (ID);

--------- inspection_line_items ------------
CREATE TABLE inspection_line_items (ID SERIAL PRIMARY KEY,
inspectionid INTEGER,
productid INTEGER,
quantitycounted INTEGER,
boxcounted INTEGER,
passquantity INTEGER,
passlocationid INTEGER,
failquantity INTEGER,
failreason INTEGER,
faillocationid INTEGER,
lotflag BOOLEAN,
dryiceflag BOOLEAN,
icepackflag BOOLEAN,
vvmflag BOOLEAN,
cccardflag BOOLEAN,
electronicdeviceflag BOOLEAN,
otherMonitor VARCHAR (255),
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE inspection_line_items ADD CONSTRAINT inspection_line_items_inspection_fkey FOREIGN KEY (inspectionid) REFERENCES inspections (ID);
ALTER TABLE inspection_line_items ADD CONSTRAINT inspection_line_items_products_fkey FOREIGN KEY (productid) REFERENCES products (ID);

---------- inspection_lots ------------
CREATE TABLE inspection_lots (ID SERIAL PRIMARY KEY,
inspectionlineitemid INTEGER,

lotNumber VARCHAR (20),
countedquantity INTEGER,
passquantity INTEGER,
passlocationid INTEGER,
failquantity INTEGER,
failreason INTEGER,
faillocationid INTEGER,
vvmstatus INTEGER,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE inspection_lots ADD CONSTRAINT inspection_lots_inspection_line_items_fkey FOREIGN KEY (inspectionlineitemid) REFERENCES inspection_line_items (ID);
ALTER TABLE inspection_lots ADD CONSTRAINT inspection_lots_passlocations_fkey FOREIGN KEY (passlocationid) REFERENCES locations (ID);
ALTER TABLE inspection_lots ADD CONSTRAINT inspection_lots_faillocations_fkey FOREIGN KEY (faillocationid) REFERENCES locations (ID);

---------- inspection_lot_problems ------------
CREATE TABLE inspection_lot_problems (ID SERIAL PRIMARY KEY,
inspectionlotid INTEGER,
boxnumber INTEGER,
lotnumber VARCHAR (20),
isalarma BOOLEAN DEFAULT false,
isalarmb BOOLEAN default false,
isalarmc BOOLEAN default false,
isalarmd BOOLEAN default false,
iscca BOOLEAN default false,
isccb BOOLEAN default false,
isccc BOOLEAN default false,
isccd BOOLEAN default false,
createdBy INTEGER,
createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


