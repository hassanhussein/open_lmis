
DROP TABLE IF EXISTS public.location_stock_card_entry_key_values;
DROP TABLE IF EXISTS public.lot_location_entries;

CREATE TABLE public.lot_location_entries
(
  id serial,
  locationId integer,
  type text,
  quantity integer,
  vvmId integer,
  stockCardId integer,
  lotId INTEGER,
  createdBy integer,
  createdDate timestamp without time zone DEFAULT now(),
  modifiedBy integer,
  modifiedDate timestamp without time zone DEFAULT now(),
  CONSTRAINT lot_location_entries_pkey PRIMARY KEY (id),
  CONSTRAINT lot_location_entries_lots_fkey FOREIGN KEY (lotId)
      REFERENCES public.lots (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
        CONSTRAINT lot_location_entries_stockcard_fkey FOREIGN KEY (stockCardId)
      REFERENCES public.stock_cards (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
      CONSTRAINT lot_location_entries_vvmId_fkey FOREIGN KEY (vvmId)
      REFERENCES public.vvm_statuses (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE public.location_stock_card_entry_key_values
(
  stockCardentryId integer NOT NULL,
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
