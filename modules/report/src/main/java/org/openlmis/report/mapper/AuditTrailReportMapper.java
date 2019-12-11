package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.openlmis.report.builder.AuditTrailQueryBuilder;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.report.AuditTrailReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditTrailReportMapper {
    @SelectProvider(type=AuditTrailQueryBuilder.class, method="getAuditTrailReportQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10, timeout=0, useCache=true, flushCache=true)
    List<AuditTrailReport> getAuditTrailReport(@Param("filterCriteria") ReportParameter reportFilterData,
                                               @Param("userId") Long userId);

    @SelectProvider(type = AuditTrailQueryBuilder.class, method = "getTotalCountQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    int getAuditTrailReportCount(
            @Param("filterCriteria") ReportParameter filterCriteria
    );
}
