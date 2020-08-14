DROP TABLE if exists public.manual_test_categorized_result_line_item;
DROP TABLE if exists public.manual_test_result_type;
DROP TABLE if exists public.manual_test_result_category;
----------------------------------------------------------------------
DROP SEQUENCE if exists public.manual_test_result_category_id_seq;
DROP  SEQUENCE  if exists  public.manual_test_result_type_id_seq;
DROP SEQUENCE  if exists  public.manual_test_result_line_item_id_seq;
--------------------------------------------------------------------------
------------------------------------------------------------------------

CREATE SEQUENCE  public.manual_test_result_category_id_seq;

ALTER SEQUENCE public.manual_test_result_category_id_seq
    OWNER TO postgres;

-----------------------------------------------------------------

CREATE SEQUENCE public.manual_test_result_type_id_seq;

ALTER SEQUENCE public.manual_test_result_type_id_seq
    OWNER TO postgres;

--------------------------------------------------------------------------

CREATE SEQUENCE public.manual_test_result_line_item_id_seq;

ALTER SEQUENCE public.manual_test_result_line_item_id_seq
    OWNER TO postgres;

------------------------------------------------------------------------------
---------------------------------------------------------------------------------

CREATE TABLE public.manual_test_result_category
(
    id integer NOT NULL DEFAULT nextval('manual_test_result_category_id_seq'::regclass),
	code character varying(100) COLLATE pg_catalog."default" NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description character varying(500) COLLATE pg_catalog."default",
	displayorder integer NOT NULL,
    createdby integer,
    modifiedby integer,
    createddate date,
    modifieddate date,
    CONSTRAINT manual_test_result_category_pkey PRIMARY KEY (id)
);

----------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------

CREATE TABLE public.manual_test_result_type
(
    id integer NOT NULL DEFAULT nextval('manual_test_result_type_id_seq'::regclass),
    resultcategoryid integer NOT NULL,
	code character varying(100) COLLATE pg_catalog."default" NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
	displayorder integer NOT NULL,
    description character varying(500) COLLATE pg_catalog."default",
    createdby integer,
    modifiedby integer,
    createddate date,
    modifieddate date,
    CONSTRAINT manual_test_result_type_pkey PRIMARY KEY (id),
    CONSTRAINT test_result_type_group_fn FOREIGN KEY (resultcategoryid)
        REFERENCES public.manual_test_result_category (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


-----------------------------------------------------------------------------------------------------



CREATE TABLE public.manual_test_categorized_result_line_item
(
    id integer NOT NULL DEFAULT nextval('manual_test_result_line_item_id_seq'::regclass),
    testresulttypeid integer NOT NULL,
	testlineitemid integer NOT NULL,
    count integer NOT NULL,
    createdby integer,
    modifiedby integer,
    createddate date,
    modifieddate date,
    CONSTRAINT manual_test_result_line_item_pkey PRIMARY KEY (id),
    CONSTRAINT manual_test_line_item_id_fk FOREIGN KEY (testlineitemid)
        REFERENCES public.manual_test_line_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT manual_test_result_type_id_fk FOREIGN KEY (testresulttypeid)
        REFERENCES public.manual_test_result_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

























