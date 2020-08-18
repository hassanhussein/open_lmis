ALTER TABLE documents
ADD COLUMN IF NOT EXISTS deleted BOOLEAN  default false;

ALTER TABLE public.documents
    ADD COLUMN IF NOT EXISTS createddate timestamp without time zone;

    ALTER TABLE public.documents
    ADD COLUMN IF NOT EXISTS createdby integer;

    ALTER TABLE public.documents
    ADD COLUMN IF NOT EXISTS comment character varying(500);


    ALTER TABLE public.documents
    ADD COLUMN IF NOT EXISTS deletionlocation character varying(40);

    ALTER TABLE public.documents
    ADD COLUMN IF NOT EXISTS deletedby integer;

    ALTER TABLE public.purchase_documents
    ADD COLUMN IF NOT EXISTS deleted boolean DEFAULT false;

    ALTER TABLE public.purchase_documents
    ADD COLUMN IF NOT EXISTS deletionlocation character varying(40);

    ALTER TABLE public.purchase_documents
    ADD COLUMN IF NOT EXISTS deletedby boolean integer false;

    ALTER TABLE public.purchase_documents
    ADD COLUMN IF NOT EXISTS comment character varying(200);


