package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.DistrictFundUtilizationStatusQueryBuilder;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.report.DistrictFundUtilizationReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DistrictFundUtilizationMapper {

    @SelectProvider(type = DistrictFundUtilizationStatusQueryBuilder.class, method = "getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = -1, timeout = 0, useCache = false, flushCache = false)
    public List<DistrictFundUtilizationReport> getDistrictFundUtilization(
            @Param("filterCriteria") ReportParameter filterCriteria,
            @Param("SortCriteria") Map<String, String[]> sortCriteria,
            @Param("rowBounds") RowBounds rowBounds,
            @Param("userId") Long userId
    );



}
