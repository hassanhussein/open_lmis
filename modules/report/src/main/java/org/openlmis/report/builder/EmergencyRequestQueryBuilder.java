package org.openlmis.report.builder;

import org.openlmis.report.model.params.NonReportingFacilityParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.periodIsFilteredBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.programIsFilteredBy;


public class EmergencyRequestQueryBuilder {

    private static final String FILTER_CRITERIA = "filterCriteria";

    public static String getQuery(Map params) {
        NonReportingFacilityParam nonReportingFacilityParam = (NonReportingFacilityParam) params.get(FILTER_CRITERIA);
        return getQueryString(nonReportingFacilityParam);
    }

    private static String getQueryString(NonReportingFacilityParam filterParam) {
        BEGIN();
        SELECT_DISTINCT("r.rnrid");
        SELECT("r.status");
        SELECT("r.programid");
        SELECT("r.zoneid");
        SELECT("r.provinceid");
        SELECT("r.districtid");
        SELECT("r.facilityid");
        SELECT("r.periodid");

        SELECT("r.program");
//        SELECT_DISTINCT("r.zone");
        SELECT("r.province");
        SELECT("r.district");
        SELECT("r.facility");
        SELECT("r.facilitycode");
        SELECT("r.period");
        SELECT("r.startdate");
        SELECT("r.enddate");
        SELECT(" o.ordernumber");
        FROM("mv_requisition r");
        LEFT_OUTER_JOIN( " orders o on r.rnrid =o.id");
        WHERE("emergency = TRUE");
        writePredicates(filterParam);
        String query = SQL();
        return query;
    }

    private static void writePredicates(NonReportingFacilityParam filterParams) {

        WHERE(programIsFilteredBy("r.programid"));
        if (filterParams.getZone() != 0) {
            WHERE("(r.zoneid =#{filterCriteria.zone}" +
                    " or r.districtid= #{filterCriteria.zone} or " +
                    " r.provinceid= #{filterCriteria.zone})");
        }
        if (filterParams.getPeriod() != 0) {
            WHERE(periodIsFilteredBy("r.periodid"));
        }

    }
}
