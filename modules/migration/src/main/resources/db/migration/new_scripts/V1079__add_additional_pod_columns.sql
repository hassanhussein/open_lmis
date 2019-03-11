DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN sourceApplication character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column sourceApplication already exists in pod.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN msdOrderNumber character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column msdOrderNumber already exists in pod.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN deliveredDate timestamp without time zone DEFAULT now();
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column deliveredDate already exists in pod';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN orderDate timestamp without time zone DEFAULT now();
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column orderDate already exists in pod.';
        END;
    END;
$$;
DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN invoiceNumber character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column invoiceNumber already exists in pod.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN invoiceDate timestamp without time zone DEFAULT now();
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column invoiceDate already exists in pod.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN deliveryStatus character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column deliveryStatus already exists in pod.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN numberOfItemsReceived integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column numberOfItemsReceived already exists in pod.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN comment character varying(400);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column comment already exists in pod.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod ADD COLUMN totalInvoiceItem integer;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column totalInvoiceItem already exists in pod.';
        END;
    END;
$$;



DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod_line_items ADD COLUMN invoiceNumber character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column invoiceNumber already exists in pod_line_items.';
        END;
    END;
$$;
DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod_line_items ADD COLUMN description character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column description already exists in pod_line_items.';
        END;
    END;
$$;
DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod_line_items ADD COLUMN comment character varying(400);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column comment already exists in pod_line_items.';
        END;
    END;
$$;

DO $$
    BEGIN
        BEGIN
            ALTER TABLE pod_line_items ADD COLUMN uom character varying(200);
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column uom already exists in pod.';
        END;
    END;
$$;
