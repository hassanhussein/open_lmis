package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.EmergencyRequestQueryBuilder;
import org.openlmis.report.builder.PendingRequisitionQueryBuilder;
import org.openlmis.report.model.params.NonReportingFacilityParam;
import org.openlmis.report.model.report.EmergencyRequistionReport;
import org.openlmis.report.model.report.PendingRequistionReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PendingRequisitionsMapper {
    @SelectProvider(type = PendingRequisitionQueryBuilder.class, method = "getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    List<PendingRequistionReport> getPendingRequisitions(@Param("filterCriteria") NonReportingFacilityParam params,
                                                           @Param("RowBounds") RowBounds rowBounds,
                                                           @Param("userId") Long userId);
}
