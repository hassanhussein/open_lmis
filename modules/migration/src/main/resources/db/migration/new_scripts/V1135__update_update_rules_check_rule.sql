-- Function: public.fn_maximum_stock_rule(integer, integer, integer, character varying)


  -- Function: public.fn_minimum_stock_rule(integer, integer, integer, character varying)




DROP FUNCTION public.fn_minimum_stock_rule(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION public.fn_minimum_stock_rule(
    IN v_facility_id integer,
    IN v_rnr_id integer,
    IN v_program_id integer,
    IN v_product character varying)
  RETURNS TABLE(maxstockrule boolean, message text) AS
$BODY$
DECLARE result record;
BEGIN
RETURN QUERY
select
CASE
WHEN
( quantityrequested < (( quantitydispensed * 2) - stockinhand ))
THEN
FALSE
ELSE
TRUE
END
,
CASE
WHEN
( quantityrequested < (( quantitydispensed * 2) - stockinhand ))
THEN
'Requesting quantities below minimum level. Your minimum stock level is  ' ||( quantitydispensed * 2) - stockinhand  || ' units and you are requesting ' || quantityrequested || ' units'::text
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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION public.fn_minimum_stock_rule(integer, integer, integer, character varying)
  OWNER TO postgres;




  DROP FUNCTION public.fn_maximum_stock_rule(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION public.fn_maximum_stock_rule(
    IN v_facility_id integer,
    IN v_rnr_id integer,
    IN v_program_id integer,
    IN v_product character varying)
  RETURNS TABLE(maxstockrule boolean, message text) AS
$BODY$
DECLARE result record;
BEGIN
RETURN QUERY
select
CASE
WHEN
( quantityrequested > (( quantitydispensed * 2) - stockinhand ))
THEN
FALSE
ELSE
TRUE
END,
CASE
WHEN
( quantityrequested > (( quantitydispensed * 2) - stockinhand ))
THEN
'Your are asking more than your maximum stock level. Your maximum stock level is ' || ( quantitydispensed * 2) - stockinhand  || ' units while you are requesting  ' || (quantityrequested) || ' units'::text
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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION public.fn_maximum_stock_rule(integer, integer, integer, character varying)
  OWNER TO postgres;



   DROP FUNCTION public.fn_rule_questionable_losses_and_adjustment(v_rnr_id integer, v_product_code character varying);

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
            > 20
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
            > 20
         THEN
            'Your adjustment ( Lost, Expires and Stolen) is ' || SUM(rlila.quantity)  || ' units which is ' ||  round(SUM(rlila.quantity) / SUM(rli.quantityreceived)::float * 100) || '% of what you quantity received ' || SUM(rli.quantityreceived) ||' units'
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




 DROP FUNCTION public.fn_rule_questionable_consumption(integer, integer, character varying);

CREATE OR REPLACE FUNCTION public.fn_rule_questionable_consumption(
    IN v_rnr_id integer,
    IN v_facility_id integer,
    IN v_product_code character varying)
  RETURNS TABLE(maxstockrule boolean, message text) AS
$BODY$
DECLARE result record;
BEGIN
RETURN QUERY
SELECT CASE
           WHEN (coalesce(quantitydispensed, 0) -
                   (SELECT NULLIF(AVG(quantitydispensed), 0)
                    FROM requisition_line_items rli
                    JOIN requisitions r ON r.id = rli.rnrid
                    WHERE productcode = v_product_code
                      AND r.facilityid = v_facility_id
                      AND r.status = 'RELEASED'
                    LIMIT 3)) /
                  (SELECT NULLIF(AVG(quantitydispensed), 0)
                   FROM requisition_line_items rli
                   JOIN requisitions r ON r.id = rli.rnrid
                   WHERE productcode = v_product_code
                     AND r.facilityid = v_facility_id
                     AND r.status = 'RELEASED'
                   LIMIT 3) * 100 > 50 THEN FALSE
           ELSE TRUE
       END,
       CASE
           WHEN (coalesce(quantitydispensed, 0) -
                   (SELECT NULLIF(AVG(quantitydispensed), 0)
                    FROM requisition_line_items rli
                    JOIN requisitions r ON r.id = rli.rnrid
                    WHERE productcode = v_product_code
                      AND r.facilityid = v_facility_id
                      AND r.status = 'RELEASED'
                    LIMIT 3)) /
                  (SELECT NULLIF(AVG(quantitydispensed), 0)
                   FROM requisition_line_items rli
                   JOIN requisitions r ON r.id = rli.rnrid
                   WHERE productcode = v_product_code
                     AND r.facilityid = v_facility_id
                     AND r.status = 'RELEASED'
                   LIMIT 3) * 100 > 50
                   THEN 'Your reported consumtion ' ||quantitydispensed  || ' unit is different from you historical avarage consumption of ' || round((SELECT NULLIF(AVG(quantitydispensed), 0)
                    FROM requisition_line_items rli
                    JOIN requisitions r ON r.id = rli.rnrid
                    WHERE productcode = v_product_code
                      AND r.facilityid = v_facility_id
                      AND r.status = 'RELEASED'
                    LIMIT 3)) ||' units'
           ELSE 'SUCCESS'
       END
FROM requisition_line_items
WHERE productcode = v_product_code
  AND rnrid = v_rnr_id;

END ;

$BODY$ LANGUAGE PLPGSQL VOLATILE COST 100 ROWS 1000;


ALTER FUNCTION public.fn_rule_questionable_consumption(integer, integer, CHARACTER varying) OWNER TO postgres;


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
--select  'Missing stock out days'as rule, * from fn_stockout_days_rule( v_rnr_item_id) UNION ALL
select  'Ordering above maximum stock level' as rule, * from fn_maximum_stock_rule(v_facility_id,v_rnr_id,v_program_id, v_product) UNION ALL
select  'Ordering minimum stock level' as rule, * from fn_minimum_stock_rule(v_facility_id,v_rnr_id,v_program_id, v_product) UNION ALL
--select 'Above allocated budget' as rule, * from fn_rule_total_cost(v_rnr_id,v_facility_id, v_product) UNION ALL
select 'Questionable consumption' as rule, * from fn_rule_questionable_consumption(v_rnr_id,v_facility_id, v_product) UNION ALL
select 'Questionable  losses and adjustment' as rule, * from fn_rule_questionable_losses_and_adjustment(v_rnr_id, v_product)
--UNION ALL select 'Skipped managed health commodities' as rule, * from fn_rule_skipped_manageable_health_commodities(v_facility_id, v_rnr_id, v_product, skipped)
;
END;
$$
LANGUAGE plpgsql;

