DROP TABLE IF EXISTS public.msd_stock_statuses;

CREATE TABLE public.msd_stock_statuses
(
    id serial,
    facilityId Integer,
    productId Integer,
    onhanddate character varying(200),
    onhandquantity character varying(200),
    UOM character varying(200),
    partDescription character varying(200),
    mos numeric(10,0) DEFAULT 0,
    createddate timestamp without time zone NOT NULL DEFAULT now(),
    createdby integer,
    CONSTRAINT msd_stock_status_pkey PRIMARY KEY (id)
);