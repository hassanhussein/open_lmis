DROP TABLE IF EXISTS stock_card_locations;
CREATE TABLE stock_card_locations (
id  SERIAL,
stockcardid INTEGER NOT NULL,
locationid INTEGER NOT NULL,
createdby INTEGER,
createddate  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedby INTEGER,
modifieddate  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT stock_card_locations_pkey PRIMARY KEY (id)
);