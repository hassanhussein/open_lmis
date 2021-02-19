CREATE MATERIALIZED VIEW covid_forecasts as
SELECT f.hfrcode as facility_id, productcode as product_code,
       CASE WHEN amc is null THEN 0::TEXT ELSE amc::text END as actual_consumed,
       to_char(pp.enddate,'YYYY-MM-dd') as period,
       normalizedconsumption::text as forecast_consumed
from requisitions r
         JOIN requisition_line_items i on r.id = i.rnrid
         JOIN facilities F on r.facilityId = F.ID
         JOIN requisition_line_item_losses_adjustments L on l.requisitionlineitemid = i.id
         JOIN processing_periods pp on r.periodId = pp.id
         JOIN losses_adjustments_types ft ON l.type = ft.name
where f.hfrcode::text is not null and f.hfrcode::text not in ('.','-') and pp.startDate::date >='2020-01-01'::date
           and r.programId = 1
ORDER BY R.ID DESC ;


CREATE MATERIALIZED VIEW covid_emergency_commodities as

select p.code as product_code, f.hfrcode as facility_id,
       to_char(r.modifieddate, 'yyyy-MM-dd') as date,
                              Case when (stockinhand is null) THEN 0 else stockinhand END
							  as available_quantity,
                       Case when (quantityReceived is null) THEN 0 else quantityReceived
END as stock_quantity,
                                case when amc > 0 then coalesce(stockinhand/ amc,0)
								else 0 end as stock_of_month
                                from requisitions r
                                JOIN requisition_line_items i On r.id = i.rnrid
                                JOIN orders o ON r.id = o.id
                               JOIN products p ON i.productcode = p.code
                                JOIN program_products pp On pp.productid = p.id
                                 JOIN processing_periods per ON r.periodiD = per.id
                                JOIN facilities f ON r.facilityId = F.ID
                                  WHERE f.hfrcode NOT IN('.','-') AND
								  f.hfrcode IS NOT NULL AND
								  per.startDate >= '2020-01-01'::date AND
								  r.programId = 1
                                 ORDER BY R.ID DESC
