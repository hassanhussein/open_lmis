package org.openlmis.report.builder;

import org.openlmis.report.model.params.QuantificationExtractReportParam;

import java.util.Map;

import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.geoZoneIsFilteredBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.scheduleFilteredBy;

public class QuantificationExtractQueryBuilder {

    private static QuantificationExtractReportParam returnParams(Map map) {
        return (QuantificationExtractReportParam) map.get("filterCriteria");
    }

    public static String getReportQuery(Map map) {
/*
        return "   SELECT \n" +
                "   periodName, period,facilityCode,facility,facilityType,code,product,category,unit, SUM(uom) uom,  sum(issues) issues, \n" +
                "   round(cast(sum(issues) / (sum(uom)/count(Code))::float as numeric),0) Consumption\n" +
                "   , ceil(sum(normalizedConsumption) / (sum(uom)/count(code))::float) adjustedConsumptionInPacks, sum(normalizedConsumption) adjustedConsumption  " +
                "   from mv_quantification_extraction\n" +
                     writePredicates(returnParams(map)) +
                "   group by periodName,period,facilityCode,facility,facilityType,code,product,category,unit" +
                " order by periodName ";*/

           return  "    SELECT pp.name periodName,PP.ID period,FT.CODE facilityCode,F.NAME facility,ft.name facilityType,p.code,p.primaryName product,pc.name category,\n" +
                   "    SUM(li.packsize) uom,\n" +
                   "   -- p.code, p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product,\n" +
                   "    sum(li.quantityDispensed) issues, \n" +
                   "    sum(li.normalizedConsumption) adjustedConsumption\n" +
                   "    ,ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks\n" +
                   "    ,ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks \n" +
                   "    FROM requisition_line_items li\n" +
                   "    INNER JOIN requisitions r on r.id = li.rnrid\n" +
                   "    INNER JOIN facilities f on r.facilityId = f.id \n" +
                   "    INNER JOIN vw_districts d on d.district_id = f.geographicZoneId \n" +
                   "    INNER JOIN processing_periods pp on pp.id = r.periodId\n" +
                   "    INNER JOIN products p on p.code::text = li.productCode::text\n" +
                   "    INNER JOIN program_products ppg on ppg.programId = r.programId and ppg.productId = p.id\n" +
                   "    INNER JOIN dosage_units ds ON ds.id = p.dosageunitid\n" +
                   "    INNER JOIN facility_types FT ON f.typeid = ft.id\n" +
                   "    INNER JOIN product_categories pc ON ppg.productCategoryId = pc.id \n" +
                   "     WHERE pp.startDate::date <= '2019-01-01'::date and pp.endDate::date < '2019-12-30'::date and pp.scheduleId=2 AND r.programiD=1 and d.district_id = 482\n" +
                   "     and r.emergency = false and pp.enableorder = true\n" +
                   "    GROUP BY p.code, p.primaryName,\n" +
                   "    pp.name, PP.ID,FT.CODE,F.NAME,ft.name, PC.NAME\n" +
                   "    ORDER BY p.primaryName LIMIT 10 ";
    }


    public static String getTotalNumberOfRowsQuery(Map params) {

        return " SELECT count(*) totalRecords\n" +
                "                            from mv_quantification_extraction "+
        writePredicates(returnParams(params));
    }

    private static String writePredicates(QuantificationExtractReportParam filter) {

        String predicate = "";
        predicate += " where " + programIsFilteredBy("program") +
                " AND " + periodStartDateRangeFilteredBy("startdate", filter.getPeriodStart().trim()) +
                " AND " + periodEndDateRangeFilteredBy("enddate", filter.getPeriodEnd().trim());

            predicate +=
                    " AND ( mv_quantification_extraction.district_id = " +  filter.getZone() + "::INT ) ";

        if (filter.getSchedule() != 0) {
            predicate += " AND " + scheduleFilteredBy("schedule");
        }
        return predicate;
    }

}
