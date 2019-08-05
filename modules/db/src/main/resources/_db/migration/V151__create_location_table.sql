DROP TABLE IF EXISTS locations;																									
DROP TABLE IF EXISTS location_types;																									
																									
CREATE TABLE location_types (																									
	id SERIAL PRIMARY KEY,																								
	code VARCHAR (50) NOT NULL UNIQUE,																								
	name VARCHAR (100),																								
	description VARCHAR (200),																								
	active BOOLEAN,																								
	createdBy INTEGER,																								
	createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,																								
	modifiedBy INTEGER,																								
	modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP																								
);																									
																									
CREATE UNIQUE INDEX uc_location_types_lower_code ON location_types (LOWER(code));																									
																									
CREATE TABLE locations (																									
	id SERIAL PRIMARY KEY,																								
	code VARCHAR (100) NOT NULL UNIQUE,							 	 																
	name VARCHAR (200),			 																					
	active BOOLEAN,								 																
	typeid INTEGER NOT NULL,																								
	zoneid VARCHAR (50),							 																	
	size VARCHAR (20),																								
	capacity NUMERIC (8, 4),																								
	aisleid VARCHAR (50),																								
	 parentid INTEGER,																								
	createdBy INTEGER,																								
	createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,																								
	modifiedBy INTEGER,																								
	modifiedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP																								
);																									
																									
CREATE UNIQUE INDEX uc_locations_lower_code ON locations (LOWER(code));																									
																									
/* add foreign keys */																									
ALTER TABLE locations ADD CONSTRAINT locations_location_types_fkey FOREIGN KEY (typeid) REFERENCES location_types (ID);																									
ALTER TABLE locations ADD CONSTRAINT locations_parentid_fkey FOREIGN KEY (parentid) REFERENCES locations (id);																									
																									