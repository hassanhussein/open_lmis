DROP MATERIALIZED VIEW public.mv_latest_reported_stock_status;
CREATE MATERIALIZED VIEW public.mv_latest_reported_stock_status AS
select
 rli.productcode,
 p.id as productid,
 p.tracer,
 p.primaryname,
 gz.district_name,
 gz.region_name,
 SUM(CASE WHEN stockinhand=0 THEN 1 ELSE 0 END) as stockOutIncidence,
 SUM(CASE WHEN rli.skipped=true THEN 1 ELSE 0 END) as skipped,
 MAX(pp.id) as periodid,
  count(*) as totalIncidence
 from requisition_line_items  rli
 join products p on p.code=rli.productcode
 join requisitions r on r.id=rli.rnrid
 join processing_periods pp on pp.id=r.periodid
 join facilities f on f.id=r.facilityid
 JOIN vw_districts gz ON gz.district_id = f.geographiczoneid
 where (r.id, r.facilityid)  in (select MAX(id), facilityid from requisitions where status='RELEASED' and emergency=false group by facilityid)
 group by rli.productcode,p.id, p.tracer, p.primaryname, gz.district_name,  gz.region_name
WITH NO DATA;

CREATE INDEX mv_latest_reported_stock_status_productcode_index
  ON public.mv_latest_reported_stock_status
  USING btree
  (productcode);


CREATE INDEX mv_latest_reported_stock_status_district_name_index
  ON public.mv_latest_reported_stock_status
  USING btree
  (district_name);