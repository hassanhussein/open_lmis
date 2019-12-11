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
select 'Questionable  losses and adjustment' as rule, * from fn_rule_questionable_losses_and_adjustment(v_rnr_id, v_product)
--UNION ALL select 'Skipped managed health commodities' as rule, * from fn_rule_skipped_manageable_health_commodities(v_facility_id, v_rnr_id, v_product, skipped)
;
END;
$$
LANGUAGE plpgsql




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
                  f.id = v_facility_id and p.tracer=true
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