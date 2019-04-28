package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.ItemFillRateQueryBuilder;
import org.openlmis.report.model.params.ItemFillRateReportParam;
import org.openlmis.report.model.report.ItemFillRateReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemFillRateReportMapper {

    @SelectProvider(type = ItemFillRateQueryBuilder.class, method = "getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = -1, timeout = 0, useCache = false, flushCache = false)
    public List<ItemFillRateReport> getFilteredItemFillRateReport(
            @Param("filterCriteria") ItemFillRateReportParam itemFillRateReportParam,
            @Param("rowBounds") RowBounds rowBounds,
            @Param("userId") Long userId
    );

}
