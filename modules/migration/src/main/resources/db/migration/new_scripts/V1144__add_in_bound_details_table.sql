DROP TABLE IF EXISTS public.in_bound_details;

DROP TABLE IF EXISTS public.in_bounds;

CREATE TABLE public.in_bounds
(
  id serial,
  ponumber character varying(255),
  expectedarrivaldate timestamp without time zone,
  status character varying(20),
  receivingLocationCode character varying(200),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT in_bounds_pkey PRIMARY KEY (id)
);


CREATE TABLE public.in_bound_details
(
  id serial,
  inBoundId integer,
  productCode timestamp without time zone,
  productName character varying(20),
  UOM character varying(200),
  quantityOrdered integer,
  source character varying(200),
  fundValues numeric(15,2),
  createdby integer,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT in_bound_details_pkey PRIMARY KEY (id),

  CONSTRAINT in_bound_details_fkey FOREIGN KEY (inBoundId)
      REFERENCES public.in_bounds (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);




DELETE from role_rights where rightName = 'UPLOAD_SHIPMENT_DETAILS';
DELETE from rights where name = 'UPLOAD_SHIPMENT_DETAILS';

INSERT INTO rights (name, rightType, displayNameKey, displayOrder, description) VALUES
('UPLOAD_SHIPMENT_DETAILS','REPORT','right.upload.in.bound', 109,'Permission to upload in bounds');