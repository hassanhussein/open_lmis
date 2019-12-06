--AMC
 DROP FUNCTION public.fn_amc(v_facility_id integer, v_program_id integer, v_product_id character varying);
CREATE
OR
replace FUNCTION PUBLIC.fn_amc(v_facility_id integer, v_program_id integer, v_product_id character varying)
returns integer AS
$BODY$
DECLARE
v_amc integer;
BEGIN
select COALESCE(AVG(normalizedConsumption), 0) as amc INTO v_amc from (SELECT RLI.id, max(RLI.normalizedConsumption) normalizedConsumption, max(RLI.stockInHand) stockInHand FROM requisition_line_items RLI
INNER JOIN requisitions R ON R.id = RLI.rnrId
INNER JOIN processing_periods pp on pp.id = R.periodid
AND R.facilityId = v_facility_id
AND R.programId = v_program_id
AND RLI.productCode = v_product_id
INNER JOIN requisition_status_changes
RSC ON RSC.rnrId = R.id
AND RLI.skipped = false
AND RLI.normalizedConsumption is not null
AND RLI.normalizedConsumption > 0
AND RSC.status = 'AUTHORIZED'
AND R.emergency = false
GROUP BY RLI.id, pp.startDate
ORDER BY pp.startDate DESC LIMIT 3
) a;
RETURN v_amc;
END;
$BODY$
LANGUAGE plpgsql;



DROP FUNCTION public.fn_stockout_days_rule(v_rnr_line_id integer);
CREATE
OR
replace FUNCTION PUBLIC.fn_stockout_days_rule(v_rnr_line_id integer)
  returns table ( stockOutDaysRule boolean, message text)
AS $$
DECLARE
   result record;
BEGIN
  RETURN QUERY select CASE WHEN (stockinhand=0) AND (stockoutdays=0 ) THEN
   FALSE ELSE TRUE END as stockOutDaysRule, CASE WHEN (stockinhand=0) AND (stockoutdays=0 ) THEN
   'You have set stockout without indicating for how many days'::text ELSE 'SUCCESS'::text END as message from requisition_line_items where id=v_rnr_line_id;
END;
$$
LANGUAGE plpgsql;



---MAXIMUM STOCK

 DROP FUNCTION PUBLIC.fn_maximum_stock_rule(v_facility_id integer, v_rnr_id integer, v_program_id integer, v_product character varying) ;
CREATE
OR replace FUNCTION PUBLIC.fn_maximum_stock_rule(v_facility_id integer, v_rnr_id integer, v_program_id integer, v_product character varying) returns table ( maxStockRule boolean, message text) AS $$
DECLARE result record;
BEGIN
   RETURN QUERY
   select
      CASE
         WHEN
               (calculatedorderquantity
            > (maxmonthsofstock * (
            select
               fn_amc(v_facility_id, v_program_id, v_product))) ) AND (select fn_amc(v_facility_id, v_program_id, v_product) > 0)
            THEN
               FALSE
            ELSE
               TRUE
      END
,
      CASE
         WHEN
               (calculatedorderquantity
            > (maxmonthsofstock * (
            select
               fn_amc(v_facility_id, v_program_id, v_product) )) ) AND (select fn_amc(v_facility_id, v_program_id, v_product) > 0)
            THEN
               'Your are asking more than your maximum stock level. Your maximum stock level is ' || (maxmonthsofstock * (
               select
                  fn_amc(v_facility_id, v_program_id, v_product))) || ' units while you are requestion for  ' || (calculatedorderquantity) || ' units'::text
               ELSE
                  'SUCCESS'::text
      END
               from
                  requisition_line_items
               where
                  rnrid = v_rnr_id
                  and productcode = v_product;
END
;
$$ LANGUAGE plpgsql;



 DROP FUNCTION PUBLIC.fn_minimum_stock_rule(v_facility_id integer, v_rnr_id integer, v_program_id integer, v_product character varying);

CREATE
OR replace FUNCTION PUBLIC.fn_minimum_stock_rule(v_facility_id integer, v_rnr_id integer, v_program_id integer, v_product character varying)
returns table ( maxStockRule boolean, message text) AS $$
DECLARE result record;
BEGIN
   RETURN QUERY
   select
      CASE
         WHEN
           ( calculatedorderquantity
            < ((maxmonthsofstock / 2)* (
            select
               fn_amc(v_facility_id, v_program_id, v_product)))) AND  (select fn_amc(v_facility_id, v_program_id, v_product) > 0)
            THEN
               FALSE
            ELSE
               TRUE
      END
,
      CASE
         WHEN
               (calculatedorderquantity
            < (maxmonthsofstock/2) * (
            select
               fn_amc(v_facility_id, v_program_id, v_product) )) AND  (select fn_amc(v_facility_id, v_program_id, v_product) > 0)
            THEN
               'Requesting quantities below minimum level. Your minimum stock level is  ' || ((maxmonthsofstock / 2) * (
               select
                  fn_amc(v_facility_id, v_program_id, v_product) )) || ' units and you are requesting ' || calculatedorderquantity || ' units'::text
               ELSE
                  'SUCCESS'::text
      END
               from
                  requisition_line_items
               where
                  rnrid = v_rnr_id
                  and productcode = v_product;
END
;
$$ LANGUAGE plpgsql;


--Quantity received indivisible by MSD unit of measure (UOM).
 DROP FUNCTION PUBLIC.fn_rule_received_stock_indivisible_by_msd_unit(v_rnr_item_id integer);
CREATE
OR
replace FUNCTION PUBLIC.fn_rule_received_stock_indivisible_by_msd_unit(v_rnr_item_id integer)
 returns table ( maxStockRule boolean, message text)
AS $$
DECLARE
   result record;
BEGIN
  RETURN QUERY select CASE WHEN (rli.quantityreceived%p.packsize) > 0 THEN FALSE
  ELSE TRUE END,
  CASE WHEN (rli.quantityreceived%p.packsize) > 0 THEN 'Quantity received indivisible by MSD unit of measure (UOM)'::text
  ELSE 'SUCCESS'::text END
  from requisition_line_items rli
join products p on p.code= rli.productcode
where rli.id=v_rnr_item_id;
END;
$$
LANGUAGE plpgsql;



--Skipped manageable health commodities.
DROP FUNCTION PUBLIC.fn_rule_skipped_manageable_health_commodities(v_facility_id integer, v_rnr_id integer, v_product character varying, skipped boolean) ;
CREATE
OR replace FUNCTION PUBLIC.fn_rule_skipped_manageable_health_commodities(v_facility_id integer, v_rnr_id integer, v_product character varying, skipped boolean) returns table ( maxStockRule boolean, message text) AS $$
DECLARE result record;
BEGIN
   RETURN QUERY
   select
      CASE
         WHEN
            (v_product IN
            (
               select
                  p.code
               from
                  facility_approved_products fap
                  join
                     program_products pp
                     on pp.id = fap.programproductid
                  join
                     products p
                     on p.id = pp.productid
                  join
                     facilities f
                     on f.typeid = fap.facilitytypeid
               where
                  f.id = v_facility_id
            )) AND skipped
         THEN
            FALSE
         ELSE
            TRUE
            END,
            CASE
         WHEN
            (v_product IN
            (
               select
                  p.code
               from
                  facility_approved_products fap
                  join
                     program_products pp
                     on pp.id = fap.programproductid
                  join
                     products p
                     on p.id = pp.productid
                  join
                     facilities f
                     on f.typeid = fap.facilitytypeid
               where
                  f.id = v_facility_id
            )) AND skipped
         THEN
            'Skipped manageable health commodities'
         ELSE
            'SUCCESS'
      END
;
END
;
$$ LANGUAGE plpgsql;


--Questionable Losses & Adjustment (items, reason, quantity) THRESHOLD?.

DROP FUNCTION PUBLIC.fn_rule_questionable_losses_and_adjustment(v_rnr_id integer, v_product_code character varying);
CREATE
OR replace FUNCTION PUBLIC.fn_rule_questionable_losses_and_adjustment(v_rnr_id integer, v_product_code character varying) returns table ( maxStockRule boolean, message text) AS $$
DECLARE result record;
BEGIN
   RETURN QUERY
   select
      CASE
         WHEN
            (
               SUM(rlila.quantity) / SUM(rli.quantityreceived)::float * 100
            )
            > 40
         THEN
            FALSE
         ELSE
            TRUE
      END
,
      CASE
         WHEN
            (
               SUM(rlila.quantity) / SUM(rli.quantityreceived)::float * 100
            )
            > 40
         THEN
            'Questionable Losses & Adjustment (items, reason, quantity)'
         ELSE
            'SUCCESS'
      END
   from
      requisition_line_items rli
      join
         requisition_line_item_losses_adjustments rlila
         on rli.id = rlila.requisitionlineitemid
   where
      rli.rnrid = v_rnr_id
      and rli.productcode = v_product_code
      and rlila.type IN
      (
         'EXPIRED', 'LOST', 'STOLEN'
      )
;
END
;
$$ LANGUAGE plpgsql;



--Questionable consumption (too high/too low compared to previous orders). THRESHOLD?

DROP FUNCTION PUBLIC.fn_rule_questionable_consumption(v_rnr_id integer, v_facility_id integer, v_product_code CHARACTER varying);
CREATE
OR REPLACE FUNCTION PUBLIC.fn_rule_questionable_consumption(v_rnr_id integer, v_facility_id integer, v_product_code CHARACTER varying) RETURNS TABLE (maxStockRule boolean, message text) AS $$
DECLARE result record;
BEGIN
   RETURN QUERY
   select
      CASE
         WHEN
            (
               coalesce(quantitydispensed, 0) - (
               select
                 NULLIF(AVG(quantitydispensed),0)
               from
                  requisition_line_items rli
                  join
                     requisitions r
                     on r.id = rli.rnrid
               where
                  productcode = v_product_code
                  and r.facilityid = v_facility_id
                  and r.status = 'RELEASED' limit 3)
            )
             / (
            select
               NULLIF(AVG(quantitydispensed),0)
            from
               requisition_line_items rli
               join
                  requisitions r
                  on r.id = rli.rnrid
            where
               productcode = v_product_code
               and r.facilityid = v_facility_id
               and r.status = 'RELEASED' limit 3) * 100 > 40
            THEN
               FALSE
            ELSE
               TRUE
      END
,
      CASE
         WHEN
            (
               coalesce(quantitydispensed, 0) - (
               select
                  NULLIF(AVG(quantitydispensed),0)
               from
                  requisition_line_items rli
                  join
                     requisitions r
                     on r.id = rli.rnrid
               where
                  productcode = v_product_code
                  and r.facilityid = v_facility_id
                  and r.status = 'RELEASED' limit 3)
            )
             / (
            select
               NULLIF(AVG(quantitydispensed),0)
            from
               requisition_line_items rli
               join
                  requisitions r
                  on r.id = rli.rnrid
            where
               productcode = v_product_code
               and r.facilityid = v_facility_id
               and r.status = 'RELEASED' limit 3) * 100 > 40
            THEN
               'Questionable consumption (too high/too low compared to previous orders)'
            ELSE
               'SUCCESS'
      END
            from
               requisition_line_items
            where
               productcode = v_product_code
               and rnrid = v_rnr_id;
END
;
$$ LANGUAGE PLPGSQL;




----Too high or too low total R&R value. - THRESHOLD?
DROP FUNCTION PUBLIC.fn_rule_total_cost(v_rnr_id integer,v_facility_id integer, v_product_code character varying);
CREATE
OR
replace FUNCTION PUBLIC.fn_rule_total_cost(v_rnr_id integer,v_facility_id integer, v_product_code character varying)
 returns table ( maxStockRule boolean, message text)
AS $$
DECLARE
   result record;
BEGIN
  RETURN QUERY select CASE WHEN (fullsupplyitemssubmittedcost + nonfullsupplyitemssubmittedcost) > allocatedbudget THEN FALSE
ELSE TRUE END, CASE WHEN (fullsupplyitemssubmittedcost + nonfullsupplyitemssubmittedcost) > allocatedbudget THEN  'Too high or too low total R&R value.'
ELSE 'SUCCESS' END
from requisitions where id=v_rnr_id;
END;
$$
LANGUAGE plpgsql;



--MAIN FUNCTION
DROP FUNCTION PUBLIC.fn_rnr_line_item_health_check(v_facility_id integer, v_rnr_id integer, v_rnr_item_id integer, v_program_id integer,
 v_product character varying, skipped boolean);
CREATE OR REPLACE FUNCTION PUBLIC.fn_rnr_line_item_health_check(v_facility_id integer, v_rnr_id integer, v_rnr_item_id integer, v_program_id integer,
 v_product character varying, skipped boolean)
  returns table ( rule text, stockOutDaysRule boolean, message text)
AS $$
DECLARE
   result record;
BEGIN
         RETURN QUERY
select  'Quantity received indivisible by MSD unit of measure 'as rule, * from  fn_rule_received_stock_indivisible_by_msd_unit(v_rnr_item_id) UNION ALL
select  'Missing stock out days'as rule, * from fn_stockout_days_rule( v_rnr_item_id) UNION ALL
select  'Ordering above maximum stock level' as rule, * from fn_maximum_stock_rule(v_facility_id,v_rnr_id,v_program_id, v_product) UNION ALL
select  'Ordering minimum stock level' as rule, * from fn_minimum_stock_rule(v_facility_id,v_rnr_id,v_program_id, v_product) UNION ALL
select 'Above allocated budget' as rule, * from fn_rule_total_cost(v_rnr_id,v_facility_id, v_product) UNION ALL
select 'Questionable consumption' as rule, * from fn_rule_questionable_consumption(v_rnr_id,v_facility_id, v_product) UNION ALL
select 'Questionable  losses and adjustment' as rule, * from fn_rule_questionable_losses_and_adjustment(v_rnr_id, v_product) UNION ALL
select 'Skipped managed health commodities' as rule, * from fn_rule_skipped_manageable_health_commodities(v_facility_id, v_rnr_id, v_product, skipped)
;
END;
$$
LANGUAGE plpgsql
