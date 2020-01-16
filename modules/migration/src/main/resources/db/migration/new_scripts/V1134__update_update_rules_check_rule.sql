-- Function: public.fn_maximum_stock_rule(integer, integer, integer, character varying)

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
(quantityrequested > (maxmonthsofstock * (select fn_amc(v_facility_id, v_program_id, v_product))) ) AND (select fn_amc(v_facility_id, v_program_id, v_product) > 0)
THEN
FALSE
ELSE
TRUE
END,
CASE
WHEN
(quantityrequested
> (maxmonthsofstock * (
select
fn_amc(v_facility_id, v_program_id, v_product) )) ) AND (select fn_amc(v_facility_id, v_program_id, v_product) > 0)
THEN
'Your are asking more than your maximum stock level. Your maximum stock level is ' || (maxmonthsofstock * (
select
fn_amc(v_facility_id, v_program_id, v_product))) || ' units while you are requestion for  ' || (quantityrequested) || ' units'::text
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
( quantityrequested
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
(quantityrequested
< (maxmonthsofstock/2) * (
select
fn_amc(v_facility_id, v_program_id, v_product) )) AND  (select fn_amc(v_facility_id, v_program_id, v_product) > 0)
THEN
'Requesting quantities below minimum level. Your minimum stock level is  ' || ((maxmonthsofstock / 2) * (
select
fn_amc(v_facility_id, v_program_id, v_product) )) || ' units and you are requesting ' || quantityrequested || ' units'::text
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



  -- Function: public.fn_rule_questionable_consumption(integer, integer, character varying)

-- Function: public.fn_rule_questionable_consumption(integer, integer, character varying)

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
and r.status = 'RELEASED' limit 3) * 100 > 50
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
and r.status = 'RELEASED' limit 3) * 100 > 50
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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION public.fn_rule_questionable_consumption(integer, integer, character varying)
  OWNER TO postgres;
