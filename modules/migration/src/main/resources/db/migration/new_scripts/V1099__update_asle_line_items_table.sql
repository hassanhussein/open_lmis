DROP TABLE IF EXISTS wms_warehouse_zones;
CREATE TABLE public.wms_warehouse_zones
(
  id serial,
  warehouseId integer NOT NULL,
  zoneId integer NOT NULL,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT wms_warehouse_zone_pkey PRIMARY KEY (id),
  CONSTRAINT wms_warehouse_zone_WAREHOUSE_fkey FOREIGN KEY (warehouseId)
      REFERENCES public.wms_warehouseS (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,

      CONSTRAINT wms_warehouse_zone_zoneS_fkey FOREIGN KEY (zoneId)
      REFERENCES public.wms_zones (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


DROP TABLE IF EXISTS public.wms_aisle_line_items;

CREATE TABLE public.wms_aisle_line_items
(
  id serial,
  zoneid integer NOT NULL,
  aislecode character varying(100) NOT NULL,
  binlocationfrom character varying(100) NOT NULL,
  binlocationto character varying(100) NOT NULL,
  beamlevelfrom character varying(100) NOT NULL,
  beamlevelto character varying(100) NOT NULL,
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT wms_aisle_line_items_pkey PRIMARY KEY (id),
  CONSTRAINT wms_zones_wms_aisle_line_items_fkey FOREIGN KEY (zoneid)
      REFERENCES public.wms_zones (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)