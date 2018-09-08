-- View: mv_item_fill_rate

DROP MATERIALIZED VIEW if exists mv_item_fill_rate;

CREATE MATERIALIZED VIEW mv_item_fill_rate
TABLESPACE pg_default
AS
with ifr as (
select r.programid, r.periodid, r.facilityid, l.productcode, r.id rnrid, l.quantityapproved, s.quantityshipped,
round(s.quantityshipped::numeric / quantityapproved * 100,1) item_fill_rate,
l.quantityapproved - s.quantityshipped quantityinshort,
round((l.quantityapproved - s.quantityshipped::numeric) / l.quantityapproved * 100,1) short_supply_rate

from requisition_line_items l
join requisitions r on r.id = l.rnrid
join shipment_line_items s on s.orderid = r.id
and s.productcode = l.productcode
where l.quantityapproved > 0 )
select
ifr.*
from ifr
WITH DATA;