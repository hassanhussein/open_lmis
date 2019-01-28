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

        return "   SELECT \n" +
                "   periodName, period,facilityCode,facility,facilityType,code,product,category,unit, SUM(uom) uom,  sum(issues) issues, \n" +
                "   round(cast(sum(issues) / (sum(uom)/count(Code))::float as numeric),2) Consumption\n" +
                "   from mv_quantification_extraction\n" +
                     writePredicates(returnParams(map)) +
                "   group by periodName,period,facilityCode,facility,facilityType,code,product,category,unit" +
                " order by periodName limit 2000 ";


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

            predicate +=   " and (mv_quantification_extraction.parent = " + filter.getZone() + "::INT or  mv_quantification_extraction.region_id = " +  filter.getZone() + "::INT " +
                    " or  mv_quantification_extraction.district_id = " +  filter.getZone() + "::INT " +
                    " or  mv_quantification_extraction.zone_id = " +  filter.getZone() + "::INT " +
                    "  or  0 = " + filter.getZone() + "::INT) ";

        if (filter.getSchedule() != 0) {
            predicate += " AND " + scheduleFilteredBy("schedule");
        }
        return predicate;
    }

}
