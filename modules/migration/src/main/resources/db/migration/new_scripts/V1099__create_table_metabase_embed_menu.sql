DROP TABLE if exists public.metabase_page;
DROP TABLE if exists public.embed_menu;

DROP SEQUENCE if exists public.metabase_page_id_seq;
DROP SEQUENCE if exists public.embed_menu_id_seq;

CREATE SEQUENCE public.metabase_page_id_seq;
CREATE SEQUENCE public.embed_menu_id_seq;


CREATE TABLE public.embed_menu
(
    id integer NOT NULL DEFAULT nextval('embed_menu_id_seq'::regclass),
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    parentMenu integer,
    icon character varying(50) COLLATE pg_catalog."default",
	menuitem character varying(50) COLLATE pg_catalog."default",
	description character varying(500) COLLATE pg_catalog."default",
    createdby integer,
    createddate timestamp without time zone DEFAULT now(),
    modifiedby integer,
    modifieddate timestamp without time zone DEFAULT now(),
    CONSTRAINT embed_menu_pkey PRIMARY KEY (id),
   CONSTRAINT embed_menu_parentid_fkey FOREIGN KEY (parentid)
        REFERENCES public.embed_menu(id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION

);


CREATE TABLE public.metabase_page
(
    id integer NOT NULL DEFAULT nextval('metabase_page_id_seq'::regclass),
    name character varying(200) COLLATE pg_catalog."default" NOT NULL,
    menuid integer,
    icon character varying(50) COLLATE pg_catalog."default",
	urllink character varying(50) COLLATE pg_catalog."default",
	rights character varying(500) COLLATE pg_catalog."default",
	description character varying(500) COLLATE pg_catalog."default",
    createdby integer,
    createddate timestamp without time zone DEFAULT now(),
    modifiedby integer,
    modifieddate timestamp without time zone DEFAULT now(),
    CONSTRAINT metabase_page_pkey PRIMARY KEY (id),
   CONSTRAINT metabase_page_menuid_fkey FOREIGN KEY (menuid)
        REFERENCES public.embed_menu(id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION

);

