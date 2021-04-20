package org.openlmis.report.builder;

import org.openlmis.report.model.params.QuantificationExtractReportParam;
import org.openlmis.report.model.params.RejectedRnRReportParam;

import java.util.Map;

import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.scheduleFilteredBy;

public class RejectedRnRReportQueryBuilder {

  public static String getQuery(Map params) {

    RejectedRnRReportParam filter = (RejectedRnRReportParam) params.get("filterCriteria");

    return " select zone_name as zoneName, to_char(createdDate, 'yyyy_mm') as " +
            "month_name, count(*)  rejectionCount  from ( \n" +
            "    select count(*), max(c.createdDate) as createdDate, rnrid, zone_name from requisition_status_changes c \n" +
            "    join requisitions r on r.id = c.rnrid \n" +
            "    join programs on programs.id = r.programid\n" +
            "    join facilities f on f.id = r.facilityid \n" +
            "    join vw_districts d on d.district_id = f.geographiczoneid \n" +

            writePredicates(filter) +

            "    group by rnrid, d.zone_name having count(*) > 1\n" +
            ") a\n" +
            "group by a.zone_name, to_char(createdDate, 'yyyy_mm')\n" +
            "order by to_char(createdDate, 'yyyy_mm')";
  }

  private static String writePredicates(RejectedRnRReportParam filter) {

    String predicate = "";
    predicate += " where c.status = 'INITIATED' and " + programIsFilteredBy("r.programId");

    return predicate;
  }

  private static String writePredicates2(RejectedRnRReportParam filter) {

    String predicate = "";
    predicate += " where lower(c.status)=lower('" + filter.getStatus() + "')::text and " + programIsFilteredBy("r.programId") +
            " AND " + periodIsFilteredBy("r.periodId");

    predicate += " and (d.parent = " + filter.getZone() + "::INT or  d.region_id = " + filter.getZone() + "::INT " +
            " or  d.district_id = " + filter.getZone() + "::INT " +
            " or  d.zone_id = " + filter.getZone() + "::INT " +
            "  or  0 = " + filter.getZone() + "::INT) ";

    return predicate;
  }

  public static String getRejectionsByZone(Map params) {

    RejectedRnRReportParam filter = (RejectedRnRReportParam) params.get("filterCriteria");

   return "select  district_name districtName,region_name as regionName," +
            "zone_name as zoneName," +
            " SUM(CASE WHEN count > 1 THEN 1 ELSE 0 END) as rejectionCount,  COUNT(*) as all " +
            "from " +
            "(select rnrid, count(*) from requisition_status_changes c \n" +
            "  where \n" +
            "  lower(c.status)=lower('INITIATED')::text  \n" +
            "  group by rnrid ) c\n" +
            "  join requisitions r on r.id = c.rnrid \n" +
            "  join facilities f on f.id = r.facilityid \n" +
            "  join vw_districts d on d.district_id = f.geographiczoneid \n" +
            getFilter(filter)  +
            "  group by  d.zone_name,d.district_id ,d.district_name,d.region_name \n";
  }

  public static String getFilter(RejectedRnRReportParam filter) {
    String predicate = "";
    predicate += " where " + programIsFilteredBy("r.programId") +
            " AND " + periodIsFilteredBy("r.periodId");

    predicate += " and (d.parent = " + filter.getZone() + "::INT or  d.region_id = " + filter.getZone() + "::INT " +
            " or  d.district_id = " + filter.getZone() + "::INT " +
            " or  d.zone_id = " + filter.getZone() + "::INT " +
            "  or  0 = " + filter.getZone() + "::INT) ";
    return predicate;
  }
}


