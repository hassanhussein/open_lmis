package org.openlmis.report.builder;

import org.openlmis.report.model.params.AuditTrailReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class AuditTrailQueryBuilder {

    public static String getAuditTrailReportQuery(Map params) {
        AuditTrailReportParam filter = (AuditTrailReportParam) params.get("filterCriteria");

        BEGIN();
        SELECT("action, userfullname as actionPerformedBy, identity, identityvalue");
        SELECT("oldvalue, newvalue, createddate");
        FROM("audit_trails");
        WHERE(startDateFilteredBy("createddate::date", filter.getPeriodStart()));
        WHERE(endDateFilteredBy("createddate::date", filter.getPeriodEnd()));

        if (filter.getAction() != null) {
            WHERE("split_part(action, '.', 2) = #{filterCriteria.action}");
        }
        ORDER_BY("createddate desc");

        String query = SQL();
        query = query +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;
    }

    public static String getTotalCountQuery(Map params) {
        AuditTrailReportParam filter = (AuditTrailReportParam) params.get("filterCriteria");

        BEGIN();
        SELECT("count(*)");
        FROM("audit_trails");
        WHERE(startDateFilteredBy("createddate", filter.getPeriodStart()));
        WHERE(endDateFilteredBy("createddate", filter.getPeriodEnd()));

        if (filter.getAction() != null) {
            WHERE("split_part(action, '.', 2) = #{filterCriteria.action}");
        }
        return SQL();
    }
}
