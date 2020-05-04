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
        SELECT("at.action");
        SELECT("at.userfullname  as actionPerformedBy");
        SELECT("at.userid");
        SELECT("at.oldvalue");
        SELECT("at.newvalue");
        SELECT("at.createddate");
        SELECT("case when split_part(at.action, '.', 1) = 'User' then 'USERNAME' else 'CODE' end as identity");
        SELECT("concat(p.code, f.code, u.username) as identityvalue");
        FROM("audit_trails at");
        LEFT_OUTER_JOIN("users u on (split_part(at.action, '.', 1) = 'User') and identityvalue = u.id::varchar");
        LEFT_OUTER_JOIN("products p on (split_part(at.action, '.', 1) = 'Product') and identityvalue = p.id::varchar");
        LEFT_OUTER_JOIN("facilities f on (split_part(at.action, '.', 1) = 'Facility') and identityvalue = f.id::varchar");

        WHERE(startDateFilteredBy("at.createddate::date", filter.getPeriodStart()));
        WHERE(endDateFilteredBy("at.createddate::date", filter.getPeriodEnd()));

        if (filter.getAction() != null) {
            if(filter.getAction().equals("CHANGE_NAME"))
                WHERE("split_part(at.action, '.', 2) = #{filterCriteria.action} or " +
                        "at.action = 'Product.CHANGE_PRIMARYNAME' ");
            else
                WHERE("split_part(at.action, '.', 2) = #{filterCriteria.action}");
        }

        ORDER_BY("at.createddate desc");

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

        WHERE(startDateFilteredBy("createddate::date", filter.getPeriodStart()));
        WHERE(endDateFilteredBy("createddate::date", filter.getPeriodEnd()));

        if (filter.getAction() != null) {
            if(filter.getAction().equals("CHANGE_NAME"))
                WHERE("split_part(action, '.', 2) = #{filterCriteria.action} or " +
                        "action = 'Product.CHANGE_PRIMARYNAME' ");
            else
                WHERE("split_part(action, '.', 2) = #{filterCriteria.action}");
        }

        return SQL();
    }
}
