// wms_sites

DROP TABLE IF EXISTS wms_sites;

CREATE TABLE wms_sites (

ID SERIAL,
regionId Integer not null,
code character varying(100) NOT NULL,
name character varying(200) NOT NULL,
latitude character varying(100),
longitude character varying(100),
createdby integer,
createddate timestamp without time zone DEFAULT now(),
modifiedby integer,
modifieddate timestamp without time zone DEFAULT now(),
CONSTRAINT wms_sites_PKEY PRIMARY KEY(id),
CONSTRAINT wms_sites_UNIQUE_KEY UNIQUE(code),
CONSTRAINT wms_sites_region_fkey FOREIGN KEY (regionId)
      REFERENCES public.geographic_zones (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

);



//Warehouse

DROP TABLE IF EXISTS wms_warehouses;

CREATE TABLE wms_warehouses (

ID SERIAL,
siteId Integer not null,
code character varying(100) NOT NULL,
name character varying(200) NOT NULL,
productTypeId Integer not null,
active boolean default true,
createdby integer,
createddate timestamp without time zone DEFAULT now(),
modifiedby integer,
modifieddate timestamp without time zone DEFAULT now(),
CONSTRAINT wms_warehouses_PKEY PRIMARY KEY(id),
CONSTRAINT wms_warehouses_UNIQUE_KEY UNIQUE(code),
CONSTRAINT wms_sites_warehouses_fkey FOREIGN KEY (siteId)
      REFERENCES public.wms_sites (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

);

//Zones

DROP TABLE IF EXISTS WMS_ZONES;
CREATE TABLE WMS_ZONES (
ID SERIAL,
code character varying(100) NOT NULL,
name character varying(100) NOT NULL,
description character varying(100) NOT NULL,
createdby integer,
createddate timestamp without time zone DEFAULT now(),
modifiedby integer,
modifieddate timestamp without time zone DEFAULT now(),
CONSTRAINT WMS_ZONES_PKEY PRIMARY KEY(id),
CONSTRAINT WMS_ZONES_UNIQUE_KEY UNIQUE(code)

);


//Line Items


DROP TABLE IF EXISTS wms_warehouse_line_items;

CREATE TABLE wms_warehouse_line_items (

ID SERIAL,
warehouseId Integer not null,
zoneId Integer not null,
aisleCode character varying(100) NOT NULL,
binLocationFrom character varying(100) NOT NULL,
binLocationTo character varying(100) NOT NULL,
beamLevelFrom character varying(100) NOT NULL,
beamLevelTo character varying(100) NOT NULL,
createdby integer,
createddate timestamp without time zone DEFAULT now(),
modifiedby integer,
modifieddate timestamp without time zone DEFAULT now(),
CONSTRAINT wms_warehouse_line_items_PKEY PRIMARY KEY(id),
CONSTRAINT wms_warehouses_lineItem_fkey FOREIGN KEY (warehouseId)
      REFERENCES public.wms_warehouses (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,

      CONSTRAINT wms_zones_lineItem_fkey FOREIGN KEY (zoneId)
      REFERENCES public.wms_zones (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

);


DROP TABLE if exists wms_locations;

CREATE TABLE public.wms_locations
(
  id serial,
  warehouseid integer not null ,
  code character varying(200) NOT NULL,
  active boolean default true,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT wms_locations_pkey PRIMARY KEY (id),
    CONSTRAINT wms_locations_code_key UNIQUE (code),
  CONSTRAINT wms_warehouse_location_fkey FOREIGN KEY (warehouseid)
      REFERENCES public.wms_warehouses (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



