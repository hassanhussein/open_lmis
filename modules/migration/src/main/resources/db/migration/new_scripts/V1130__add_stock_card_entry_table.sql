
DROP TABLE IF EXISTS public.lot_location_entries;

CREATE TABLE public.lot_location_entries
(
  id serial,
  lotOnHandId INTEGER,
  locationId INTEGER,
  type text,
  quantity INTEGER,
  createdBy INTEGER,
  createdDate timestamp without time zone DEFAULT now(),
  modifiedBy INTEGER,
  modifiedDate timestamp without time zone DEFAULT now(),
  CONSTRAINT plot_location_entries_pkey PRIMARY KEY (id)
);


DROP TABLE if exists public.location_stock_card_entry_key_values;

CREATE TABLE location_stock_card_entry_key_values
(
  stockcardentryid integer NOT NULL,
  keycolumn text NOT NULL,
  valuecolumn text,
  createdby integer,
  createddate timestamp with time zone DEFAULT now(),
  modifiedby integer,
  modifieddate timestamp with time zone DEFAULT now(),
  CONSTRAINT location_stock_card_entry_key_values_pkey PRIMARY KEY (stockcardentryid, keycolumn),
  CONSTRAINT lot_location_entries_fkey FOREIGN KEY (stockcardentryid)
      REFERENCES public.lot_location_entries (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);