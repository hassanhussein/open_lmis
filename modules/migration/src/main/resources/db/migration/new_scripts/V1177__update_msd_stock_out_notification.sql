DROP TABLE if exists public.stock_out_notifications;

DROP TABLE if exists public.close_to_Expire_Items;

DROP TABLE if exists public.Rationing_Items;

DROP TABLE if exists public.In_Sufficient_Funding_Items;

DROP TABLE if exists public.Full_Filled_Items;

DROP TABLE if exists public.Phased_Out_Items;

DROP TABLE if exists public.Stock_Out_Items;

CREATE TABLE public.close_to_Expire_Items
(
    id serial,
    notificationId integer,
    itemCode character varying(200) NOT NULL,
    itemDescription character varying(200) NOT NULL,
    uom character varying(200) NOT NULL,
    quantity character varying(200) NOT NULL,
    missingItemStatus character varying(200) NOT NULL,
    createdBy integer,
    createdDate timestamp without time zone DEFAULT now(),
    modifiedBy integer,
    modifiedDate timestamp without time zone DEFAULT now(),
    CONSTRAINT close_to_Expire_Items_pkey PRIMARY KEY (id)
);


CREATE TABLE public.In_Sufficient_Funding_Items
(
    id serial,
    notificationId integer,
    itemCode character varying(200) NOT NULL,
    itemDescription character varying(200) NOT NULL,
    uom character varying(200) NOT NULL,
    quantity character varying(200) NOT NULL,
    quantityShipped character varying(200) NOT NULL,
    quantityOrdered character varying(200) NOT NULL,
    missingItemStatus character varying(200) NOT NULL,
    dueDate timestamp without time zone DEFAULT now(),
    createdBy integer,
    createdDate timestamp without time zone DEFAULT now(),
    modifiedBy integer,
    modifiedDate timestamp without time zone DEFAULT now(),
    CONSTRAINT In_Sufficient_Funding_Items_pkey PRIMARY KEY (id)
);


CREATE TABLE public.Full_Filled_Items
(
    id serial,
    notificationId integer,
    itemCode character varying(200) NOT NULL,
    itemDescription character varying(200) NOT NULL,
    uom character varying(200) NOT NULL,
    quantity character varying(200) NOT NULL,
    batchSerialNo character varying(200) NOT NULL,
    batchQuantity character varying(200) NOT NULL,
    expiryDate character varying(200) NOT NULL,
    unitPrice character varying(200) NOT NULL,
    amount character varying(200) NOT NULL,
    createdBy integer,
    createdDate timestamp without time zone DEFAULT now(),
    modifiedBy integer,
    modifiedDate timestamp without time zone DEFAULT now(),
    CONSTRAINT Full_Filled_Items_pkey PRIMARY KEY (id)
);

CREATE TABLE public.phased_Out_Items
(
    id serial,
    notificationId integer,
    itemCode character varying(200) NOT NULL,
    itemDescription character varying(200) NOT NULL,
    uom character varying(200) NOT NULL,
    quantity character varying(200) NOT NULL,
    missingItemStatus character varying(200) NOT NULL,
    createdBy integer,
    createdDate timestamp without time zone DEFAULT now(),
    modifiedBy integer,
    modifiedDate timestamp without time zone DEFAULT now(),
    CONSTRAINT phased_Out_Items_pkey PRIMARY KEY (id)
);

CREATE TABLE public.rationing_Items
(
    id serial,
    notificationId integer,
    itemCode character varying(200) NOT NULL,
    itemDescription character varying(200) NOT NULL,
    uom character varying(200) NOT NULL,
    quantity character varying(200) NOT NULL,
    quantityShipped character varying(200) NOT NULL,
    quantityOrdered character varying(200) NOT NULL,
    missingItemStatus character varying(200) NOT NULL,
    dueDate timestamp without time zone DEFAULT now(),
    createdBy integer,
    createdDate timestamp without time zone DEFAULT now(),
    modifiedBy integer,
    modifiedDate timestamp without time zone DEFAULT now()
);

CREATE TABLE public.Stock_Out_Items
(
    id serial,
    notificationId integer,
    itemCode character varying(200) NOT NULL,
    itemDescription character varying(200) NOT NULL,
    uom character varying(200) NOT NULL,
    quantity character varying(200) NOT NULL,
    quantityShipped character varying(200) NOT NULL,
    quantityOrdered character varying(200) NOT NULL,
    missingItemStatus character varying(200) NOT NULL,
    dueDate timestamp without time zone DEFAULT now(),
    createdBy integer,
    createdDate timestamp without time zone DEFAULT now(),
    modifiedBy integer,
    modifiedDate timestamp without time zone DEFAULT now()
);

CREATE TABLE public.stock_out_notifications
(
    id serial,
    quoteNumber character varying(200) NOT NULL,
    customerId character varying(200) NOT NULL,
    customerName character varying(200) NOT NULL,
    hfrCode character varying(200) NOT NULL,
    elmisOrderNumber character varying(200),
    noticationDate timestamp without time zone DEFAULT now(),
    zone character varying(200),
    comment character varying(700),
    createdBy integer,
    createdDate timestamp without time zone DEFAULT now(),
    modifiedBy integer,
    modifiedDate timestamp without time zone DEFAULT now(),
    CONSTRAINT stock_out_notifications_pkey PRIMARY KEY (id)
);
