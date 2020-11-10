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
        SELECT_DISTINCT("r.id as rnrid");
        SELECT("r.status");
        SELECT("r.programid");
        SELECT("d.zone_id zoneid");
        SELECT(" d.region_id provinceid");
        SELECT("d.district_id districtid");
        SELECT(" r.facilityid");
        SELECT("r.periodid");

        SELECT(" p.name \"program\"");
//        SELECT_DISTINCT("r.zone");
        SELECT(" d.region_name province");
        SELECT("d.district_name district");
        SELECT("f.name as facility");
        SELECT("f.code as facilitycode");
        SELECT("pp.name asperiod");
        SELECT("pp.startdate");
        SELECT("pp.enddate");
        SELECT(" o.ordernumber");
        FROM("requisitions r");
        INNER_JOIN("facilities f on r.facilityid=f.id");
        INNER_JOIN("vw_districts d on  f.geographiczoneid=d.district_id");
        INNER_JOIN("programs p on p.id= r.programid");
        INNER_JOIN("processing_periods pp on pp.id =r.periodid");
        LEFT_OUTER_JOIN( " orders o on r.id =o.id");
        WHERE("emergency = TRUE");
        writePredicates(filterParam);
        String query = SQL();
        return query;
    }

    private static void writePredicates(NonReportingFacilityParam filterParams) {

        WHERE(programIsFilteredBy("r.programid"));
        if (filterParams.getZone() != 0) {
            WHERE("( d.zone_id  =#{filterCriteria.zone}" +
                    " or d.district_id= #{filterCriteria.zone} or " +
                    " d.region_id= #{filterCriteria.zone})");
        }
        if (filterParams.getPeriod() != 0) {
            WHERE(periodIsFilteredBy("r.periodid"));
        }

    }
}
