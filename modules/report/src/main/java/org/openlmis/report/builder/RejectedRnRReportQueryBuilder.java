package org.openlmis.report.builder;

import org.openlmis.report.model.params.RejectedRnRReportParam;

import java.util.Map;

import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.periodIsFilteredBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.programIsFilteredBy;

public class RejectedRnRReportQueryBuilder {

    public static String getQuery(Map params) {

        RejectedRnRReportParam filter = (RejectedRnRReportParam) params.get("filterCriteria");

        return " select districtId,district_name districtName,region_name as regionName,zone_name as zoneName, count(*) rejectedCount from ( \n" +
                "                  select count(*), rnrid, zone_name,d.district_id districtId,d.district_name,d.region_name from requisition_status_changes c \n" +
                "                    join requisitions r on r.id = c.rnrid \n" +
                "                    join facilities f on f.id = r.facilityid \n" +
                "                    join vw_districts d on d.district_id = f.geographiczoneid \n" +
                "                    where c.status = '" + filter.getStatus() + "' and r.programid =" + filter.getProgram() + " and r.periodId = " + filter.getPeriod() + " group by rnrid, d.zone_name,d.district_id ,d.district_name,d.region_name having count(*) > 1\n" +
                "                ) a\n" +
                "                group by districtId, a.district_name,a.region_name, a.zone_name\n" +
                "               \n" +
                "                order by a.region_name, a.district_name";
    }

    public static String getRejectedQuery(Map params) {
        String query = "";
        RejectedRnRReportParam filter = (RejectedRnRReportParam) params.get("filterCriteria");
        query = "   select " +
                " r.rnrid,\n" +
                "    r.status,\n" +
                "    r.createddate,\n" +
                "    r.createdby,\n" +
                "\tr.modifieddate,\n" +
                "\tr.modifiedby,\n" +
                "    r.emergency,\n" +
                "    r.allocatedbudget,\n" +
                "    r.clientsubmittedtime,\n" +
                "    r.clientsubmittednotes,\n" +
                "    r.sourceapplication,\n" +
                "    r.zoneid,\n" +
                "    r.parent,\n" +
                "    r.district,\n" +
                "    r.districtid,\n" +
                "    r.provinceid,\n" +
                "    r.province,\n" +
                "    r.facility,\n" +
                "    r.facilitytypeid,\n" +
                "    r.facilitytype,\n" +
                "    r.program,\n" +
                "    r.programid,\n" +
                "    r.periodid,\n" +
                "    r.period,\n" +
                "    r.startdate,\n" +
                "    r.enddate,\n" +
                "    r.facilitycode,\n" +
                "    r.facilityid,\n" +
                "\tr.rejectiondate,\n" +
                "\tr.statuschange,\n" +
                "    r.commenttext" +
                " from mv_rejected_requisition r " +
                "   where r.statuschange = '" + filter.getStatus() + "' " +
                "   and r.programid =" + filter.getProgram() + " " +
                "   and r.periodid = " + filter.getPeriod();
        return query;
    }

    private static String writePredicates2(RejectedRnRReportParam filter) {

        String predicate = "";
        predicate += " where lower(c.status)=lower('"+filter.getStatus()+"')::text and " + programIsFilteredBy("r.programId") +
                " AND " + periodIsFilteredBy("r.periodId") ;

        predicate +=   " and (d.parent = " + filter.getZone() + "::INT or  d.region_id = " +  filter.getZone() + "::INT " +
                " or  d.district_id = " +  filter.getZone() + "::INT " +
                " or  d.zone_id = " +  filter.getZone() + "::INT " +
                "  or  0 = " + filter.getZone() + "::INT) ";

        return predicate;
    }

    public static String getRejectionsByZone(Map params) {

        RejectedRnRReportParam filter =(RejectedRnRReportParam)params.get("filterCriteria");

        return  " select districtId,district_name districtName,region_name as regionName,zone_name as zoneName, count(*) rejectionCount from ( \n" +
                "                  select count(*), rnrid, zone_name,d.district_id districtId,d.district_name,d.region_name from requisition_status_changes c \n" +
                "                    join requisitions r on r.id = c.rnrid \n" +
                "                    join facilities f on f.id = r.facilityid \n" +
                "                    join vw_districts d on d.district_id = f.geographiczoneid \n" +
                writePredicates2(filter)
                //  "                    where c.status = 'AUTHORIZED' and r.programid ="+filter.getProgram()+" and r.periodId = "+filter.getPeriod()

                + " group by rnrid, d.zone_name,d.district_id ,d.district_name,d.region_name having count(*) > 1\n" +
                "                ) a\n" +
                "                group by districtId, a.district_name,a.region_name, a.zone_name\n" +
                "               \n" +
                "                order by a.zone_name, a.region_name, a.district_name,rejectionCount";


    }

}


