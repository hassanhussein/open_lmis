DROP table IF ExiSTS WMS_location_types;

CREATE TABLE WMS_location_types (
id serial,
code varchar(50) NOT NULL,
name varchar(255) NOT NULL
);


insert into WMS_location_types(code, name) VALUES ('a', 'A'), ('b', 'B'), ('c', 'C');


DROP table IF ExiSTS wms_locations;

CREATE TABLE wms_locations (
id serial,
code varchar(50) NOT NULL,
name varchar(255) NOT NULL,
warehouseId int4,
typeId int4
);
