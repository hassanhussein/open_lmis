
DROP TABLE IF EXISTS public.wms_transfers;

CREATE TABLE public.wms_transfers
(
  id serial,
  fromWarehouseId integer NOT NULL,
  toWarehouseId integer NOT NULL,
  fromBin integer NOT NULL,
  toBin integer NOT NULL,
  productId integer,
  transferdate timestamp without time zone DEFAULT now(),
  reasonId integer NOT NULL,
  lotNumber character varying(250),
  quantity integer,
  remarks character varying(250),
  notify boolean default true,
  createdby integer NOT NULL,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer NOT NULL,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT wms_transfers_pkey PRIMARY KEY (id),
  CONSTRAINT wms_transfers_productid_fkey FOREIGN KEY (productId)
      REFERENCES public.products (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
