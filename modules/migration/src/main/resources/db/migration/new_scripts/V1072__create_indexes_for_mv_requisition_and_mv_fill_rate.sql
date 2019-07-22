-- Index: date_index

DROP INDEX if exists public.date_index;

CREATE INDEX date_index
    ON public.mv_requisition USING btree
    (startdate, enddate)
    TABLESPACE pg_default;


	-- Index: idx_mv_requ_period

DROP INDEX if exists public.idx_mv_requ_period;

CREATE INDEX idx_mv_requ_period
    ON public.mv_requisition USING btree
    (periodid)
    TABLESPACE pg_default;


	-- Index: idx_mv_requisition_status

DROP INDEX if exists public.idx_mv_requisition_status;

CREATE INDEX idx_mv_requisition_status
    ON public.mv_requisition USING btree
    (status COLLATE pg_catalog."default")
    TABLESPACE pg_default;


	-- Index: progrogram_index

DROP INDEX if exists public.progrogram_index;

CREATE INDEX progrogram_index
    ON public.mv_requisition USING btree
    (program COLLATE pg_catalog."default")
    TABLESPACE pg_default;


-- Index: rnr_id_index

DROP INDEX if exists public.rnr_id_index;

CREATE INDEX rnr_id_index
    ON public.mv_requisition USING btree
    (rnrid)
    TABLESPACE pg_default;



-- Index: zone_index

DROP INDEX if exists public.zone_index;

CREATE INDEX zone_index
    ON public.mv_requisition USING btree
    (districtid)
    TABLESPACE pg_default;


-- Index: idx_mv_order_fill_rate_product_code

DROP INDEX if exists public.idx_mv_order_fill_rate_product_code;

CREATE INDEX idx_mv_order_fill_rate_product_code
    ON public.mv_order_fulfillment USING btree
    (productcode COLLATE pg_catalog."default")
    TABLESPACE pg_default;


-- Index: idx_order

DROP INDEX if exists public.idx_order;

CREATE INDEX idx_order
    ON public.mv_order_fulfillment USING btree
    (orderid)
    TABLESPACE pg_default;

