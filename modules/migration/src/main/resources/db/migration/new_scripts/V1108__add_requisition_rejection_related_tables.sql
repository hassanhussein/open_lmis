DROP TABLE IF exists rejection_categories CASCADE;

CREATE TABLE rejection_categories
(
  id serial,
  name character varying(250) NOT NULL,
  code character varying(250) NOT NULL,
  createdby integer NOT NULL,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer NOT NULL,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT rejection_categories_pkey PRIMARY KEY (id)
);


ALTER TABLE rejections
DROP COLUMN IF EXISTS rejectioncategoryid;

ALTER TABLE rejections
ADD COLUMN rejectioncategoryid integer;


ALTER TABLE rejections
    ADD CONSTRAINT fk_rejection_categories FOREIGN KEY (rejectioncategoryid) REFERENCES rejection_categories (id);


DROP TABLE IF EXISTS requisition_rejections;

CREATE TABLE requisition_rejections
(
  id serial,
  rnrid integer NOT NULL,
  rejectioncategoryid integer NOT NULL,
  rejectionId integer NOT NULL,
  createdby integer NOT NULL,
  createddate timestamp without time zone DEFAULT now(),
  modifiedby integer NOT NULL,
  modifieddate timestamp without time zone DEFAULT now(),
  CONSTRAINT requisition_rejections_pkey PRIMARY KEY (id),
  CONSTRAINT requisition_rejections_createdby_fkey FOREIGN KEY (createdby)
      REFERENCES public.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT requisition_rejections_modifiedby_fkey FOREIGN KEY (modifiedby)
      REFERENCES public.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT requisition_rejections_rnrid_fkey FOREIGN KEY (rnrid)
      REFERENCES public.requisitions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)



