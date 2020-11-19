DROP INDEX if exists public.idx_facilityid;

CREATE INDEX idx_facilityid
    ON public.mv_requisition USING btree
    (facilityid)
    TABLESPACE pg_default;

-- Index: idx_programid

DROP INDEX if exists public.idx_programid;

CREATE INDEX idx_programid
    ON public.mv_requisition USING btree
    (programid)
    TABLESPACE pg_default;

-- Index: idx_categoryid

DROP INDEX if exists public.idx_categoryid;

CREATE INDEX idx_categoryid
    ON public.mv_requisition USING btree
    (categoryid)
    TABLESPACE pg_default;


