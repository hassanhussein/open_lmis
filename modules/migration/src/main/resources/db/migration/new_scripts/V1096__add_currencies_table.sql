DROP TABLE IF EXISTS currencies ;

CREATE TABLE currencies
(
  id serial,
  code character varying(50),
  name character varying(50),

  CONSTRAINT currencies_pk PRIMARY KEY (id)
);

ALTER TABLE receives ADD COLUMN currencyId INTEGER;
ALTER TABLE asns ADD COLUMN currencyId INTEGER;


DELETE FROM currencies;
INSERT INTO public.currencies(
             code, name)
    VALUES ( 'USD', 'US Dollar'),
( 'TZS', 'Tanzanian Shillings'),
( 'EUR', 'European shillings');
