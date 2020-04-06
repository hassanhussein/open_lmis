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
        SELECT_DISTINCT("r.status");
        SELECT_DISTINCT("r.programid");
        SELECT_DISTINCT("r.zoneid");
        SELECT_DISTINCT("r.provinceid");
        SELECT_DISTINCT("r.districtid");
        SELECT_DISTINCT("r.facilityid");
        SELECT_DISTINCT("r.periodid");

        SELECT_DISTINCT("r.program");
//        SELECT_DISTINCT("r.zone");
        SELECT_DISTINCT("r.province");
        SELECT_DISTINCT("r.district");
        SELECT_DISTINCT("r.facility");
        SELECT_DISTINCT("r.facilitycode");
        SELECT_DISTINCT("r.period");
        SELECT_DISTINCT("r.startdate");
        SELECT_DISTINCT("r.enddate");
        FROM("mv_requisition r");
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
