package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.openlmis.report.builder.EmergencyRnrAggregateByGeozoneQueryBuilder;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.report.AuditTrailReport;
import org.openlmis.report.model.report.EmergencyRnrAggregateByGeozoneReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyRnrAggregateByGeozoneMapper {

    @SelectProvider(type=EmergencyRnrAggregateByGeozoneQueryBuilder.class, method="getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10, timeout=0, useCache=true, flushCache=true)
    List<EmergencyRnrAggregateByGeozoneReport> getEmergencyRnrAggregate(
            @Param("filterCriteria") ReportParameter reportFilterData,
            @Param("userId") Long userId);

    @SelectProvider(type = EmergencyRnrAggregateByGeozoneQueryBuilder.class, method = "getTotalCountQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    int getEmergencyRnrAggregateCount(
            @Param("filterCriteria") ReportParameter filterCriteria
    );

}