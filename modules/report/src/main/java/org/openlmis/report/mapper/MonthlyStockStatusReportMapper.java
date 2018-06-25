package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.MonthlyStockOnHandQueryBuilder;
import org.openlmis.report.model.report.MonthlyStockStatusReport;
import org.openlmis.report.model.params.MonthlyStockStatusParam;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MonthlyStockStatusReportMapper {

    @SelectProvider(type = MonthlyStockOnHandQueryBuilder.class, method = "getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
     List<MonthlyStockStatusReport> getReport(@Param("filterCriteria") MonthlyStockStatusParam params, @Param("RowBounds") RowBounds rowBounds);
}
