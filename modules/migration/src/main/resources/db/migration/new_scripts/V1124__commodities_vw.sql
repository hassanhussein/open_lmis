CREATE MATERIALIZED VIEW public.mv_commodities_details AS
select DISTINCT rli.id,rli.productcode,p.id as productid,r.id as rnrid, p.tracer as tracer, rli.product,r.programid, rli.dispensingunit, rli.productcategory, r.periodid,
r.facilityid, gz.region_name, gz.district_name, gz.zone_name,
rli.beginningbalance,
rli.quantityreceived,
(select coalesce(sum(quantity),0) from requisition_line_item_losses_adjustments where type in ('TRANSFER_IN', 'CLINIC_RETURN') and requisitionlineitemid=rli.id) as quantityIn,
(select coalesce(sum(quantity),0) from requisition_line_item_losses_adjustments where type in ('TRANSFER_OUT', 'EXPIRED', 'LOST','STOLEN') and requisitionlineitemid=rli.id) as quantityOut,
(select coalesce(sum(quantity),0) from requisition_line_item_losses_adjustments where type in ('EXPIRED', 'LOST','STOLEN') and requisitionlineitemid=rli.id) as quantityExpiredLostStolen,
rli.quantitydispensed,rli.stockinhand,
(fap.maxmonthsofstock::numeric * rli.amc ) as maxStockQuantity,
(fap.minmonthsofstock::numeric * rli.amc ) as minStockQuantity,
rli.stockoutdays,rli.price,

     CASE
            WHEN rli.stockinhand = 0 THEN 'SO'::text
            ELSE
            CASE
                WHEN rli.amc > 0 AND rli.stockinhand > 0 THEN
                CASE
                    WHEN round(rli.stockinhand::numeric / rli.amc::numeric, 2) < fap.minmonthsofstock THEN 'US'::text
                    WHEN round(rli.stockinhand::numeric / rli.amc::numeric, 2) >= fap.minmonthsofstock AND round(rli.stockinhand::numeric / rli.amc::numeric, 2) <= fap.maxmonthsofstock::numeric THEN 'SP'::text
                    WHEN round(rli.stockinhand::numeric / rli.amc::numeric, 2) > fap.maxmonthsofstock::numeric THEN 'OS'::text
                    ELSE NULL::text
                END
                ELSE 'UK'::text
            END
        END AS status
 from requisition_line_items rli
join requisitions r on r.id=rli.rnrid
join requisition_line_item_losses_adjustments rlila on rlila.requisitionlineitemid=rli.id
JOIN facilities f ON f.id = r.facilityid
JOIN facility_types ft ON ft.id = f.typeid
JOIN products p on p.code=rli.productcode
JOIN programs pg ON pg.id = r.programid
JOIN vw_districts gz ON gz.district_id = f.geographiczoneid
JOIN program_products pgp ON r.programid = pgp.programid AND p.id = pgp.productid
JOIN facility_approved_products fap ON ft.id = fap.facilitytypeid AND fap.programproductid = pgp.id
where rli.skipped=false and r.status='RELEASED'
WITH NO DATA;

CREATE INDEX mv_productcode_index
  ON public.mv_commodities_details
  USING btree
  (productcode);


CREATE INDEX mv_facilityid_index
  ON public.mv_commodities_details
  USING btree
  (facilityid);


create unique index on mv_commodities_details (id)