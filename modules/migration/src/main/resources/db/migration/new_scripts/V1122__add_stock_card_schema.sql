DROP TABLE IF EXISTS lot_on_hand_locations;
CREATE TABLE lot_on_hand_locations (
id  SERIAL,
lotonhandId INTEGER NOT NULL,
locationId INTEGER NOT NULL,
quantityonhand integer DEFAULT 0,
createdBy INTEGER,
createdDate  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
modifiedBy INTEGER,
modifiedDate  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT lot_on_hand_locations_pkey PRIMARY KEY (id)
);


DROP TABLE IF EXISTS public.putaway_line_items;

CREATE TABLE public.putaway_line_items
(
  inspectionId integer,
  lotId integer,
  fromWarehouseId integer,
  toWarehouseId integer,
  productId integer NOT NULL,
  quantity integer DEFAULT 0,
  createdby integer,
  createddate timestamp without time zone DEFAULT now()

);