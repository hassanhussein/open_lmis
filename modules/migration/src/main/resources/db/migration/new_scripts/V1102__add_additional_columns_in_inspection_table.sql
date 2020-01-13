ALTER TABLE inspections
    DROP COLUMN IF EXISTS receiptNumber;


ALTER TABLE inspections
    ADD COLUMN receiptNumber character varying(250);

ALTER TABLE inspections
    DROP COLUMN IF EXISTS descriptionOfInspection;


ALTER TABLE inspections
    ADD COLUMN descriptionOfInspection character varying(250);





    ALTER TABLE inspection_line_items
    DROP COLUMN IF EXISTS othermonintor;
       ALTER TABLE inspection_line_items
    DROP COLUMN IF EXISTS othermonitor;
        ALTER TABLE inspection_line_items
    DROP COLUMN IF EXISTS icepackflg;

        ALTER TABLE inspection_line_items

    DROP COLUMN IF EXISTS icepackflag;


ALTER TABLE inspection_line_items
    ADD COLUMN othermonitor character varying(255);

    ALTER TABLE inspection_line_items
    ADD COLUMN icepackflag boolean default false;





