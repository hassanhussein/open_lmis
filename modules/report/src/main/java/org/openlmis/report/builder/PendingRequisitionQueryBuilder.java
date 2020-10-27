package org.openlmis.report.builder;

import org.openlmis.report.model.params.NonReportingFacilityParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.periodIsFilteredBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.programIsFilteredBy;


public class PendingRequisitionQueryBuilder {

    private static final String FILTER_CRITERIA = "filterCriteria";

    public static String getQuery(Map params) {
        NonReportingFacilityParam nonReportingFacilityParam = (NonReportingFacilityParam) params.get(FILTER_CRITERIA);
        return getQueryString(nonReportingFacilityParam);
    }

    private static String getQueryString(NonReportingFacilityParam filterParam) {
        BEGIN();
        SELECT_DISTINCT("r.id AS rnrid");
        SELECT("r.status");
        SELECT("r.emergency");
        SELECT(" d.zone_id AS zoneid");
        SELECT(" d.parent");
        SELECT(" d.district_name AS district");
        SELECT(" d.district_id AS districtid");
        SELECT(" d.region_id AS provinceid");
        SELECT(" d.region_name AS province");
        SELECT("f.name AS facility");
        SELECT("f.feconfigured");
        SELECT("ft.id AS facilitytypeid");
        SELECT("ft.name AS facilitytype");
        SELECT("ft.nominalmaxmonth");
        SELECT("ft.nominaleop");
        SELECT(" ft.code AS facilitytypecode");
        SELECT("pr.name AS program");
        SELECT("pr.id AS programid");
        SELECT("pp.id AS periodid");
        SELECT("pp.name AS period");
        SELECT("pp.startdate");
        SELECT("pp.enddate");
        SELECT("pp.scheduleid");
        SELECT(" f.code AS facilitycode");
        SELECT("r.id AS facilityid");
        FROM("requisitions r");
        INNER_JOIN("JOIN processing_periods pp ON r.periodid = pp.id");
        INNER_JOIN("JOIN programs pr ON pr.id = r.programid    ");
        INNER_JOIN(" JOIN facilities f ON f.id = r.facilityid");
        INNER_JOIN("JOIN facility_types ft ON ft.id = f.typeid");
        INNER_JOIN("JOIN facility_types ft ON ft.id = f.typeid");
        INNER_JOIN(" JOIN vw_districts d ON f.geographiczoneid = d.district_id  ");
        WHERE("r.status::text = ANY (ARRAY['IN_APPROVAL'::text, 'APPROVED'::text])");
        writePredicates(filterParam);
        String query = SQL();
        return query;
    }

    private static void writePredicates(NonReportingFacilityParam filterParams) {

        WHERE(programIsFilteredBy("r.programid"));
        if (filterParams.getZone() != 0) {
            WHERE("(d.zone_id =#{filterCriteria.zone}" +
                    " or d.district_id= #{filterCriteria.zone} or " +
                    " d.region_id= #{filterCriteria.zone})");
        }
        if (filterParams.getPeriod() != 0) {
            WHERE(periodIsFilteredBy("r.periodid"));
        }

    }
}
