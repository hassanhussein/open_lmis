
DROP TABLE IF EXISTS sites;

CREATE TABLE sites (
id serial,
code varchar(50) NOT NULL,
name varchar(255) NOT NULL,
geographicZoneId int4,
longitude varchar(255),
latitude varchar(255) ,
active boolean default true

);

DROP TABLE IF EXISTS warehouses;

CREATE TABLE warehouses (
id serial,
code varchar(50) NOT NULL,
name varchar(255) NOT NULL,
siteid int4
);

