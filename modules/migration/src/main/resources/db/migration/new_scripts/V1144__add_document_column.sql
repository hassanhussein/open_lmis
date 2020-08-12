ALTER TABLE documents
DROP COLUMN IF EXISTS deleted;

ALTER TABLE documents
ADD COLUMN deleted BOOLEAN  default false;

ALTER TABLE public.documents
    DROP COLUMN IF EXISTS createdDate;

    ALTER TABLE public.documents
    ADD COLUMN createddate timestamp without time zone;

    ALTER TABLE public.documents
    DROP COLUMN IF EXISTS createdby;
    ALTER TABLE public.documents
    ADD COLUMN createdby integer;

    ALTER TABLE public.documents
    DROP COLUMN IF EXISTS comment;

    ALTER TABLE public.documents
    ADD COLUMN comment character varying(500);


    ALTER TABLE public.documents
    DROP COLUMN IF EXISTS deletionlocation;

      ALTER TABLE public.documents
    ADD COLUMN deletionlocation character varying(40);

    ALTER TABLE public.documents
    DROP COLUMN IF EXISTS deletedby;

    ALTER TABLE public.documents
    ADD COLUMN deletedby integer;

    ALTER TABLE public.purchase_documents
    DROP COLUMN IF EXISTS deleted;

     ALTER TABLE public.purchase_documents
    ADD COLUMN deleted boolean DEFAULT false;




