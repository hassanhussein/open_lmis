package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.ProductListReportQueryBuilder;
import org.openlmis.report.model.report.ProductListReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductListReportMapper {

    @SelectProvider(type = ProductListReportQueryBuilder.class, method = "getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = -1, timeout = 0, useCache = false, flushCache = false)
    public List<ProductListReport> getFilteredSortedProductListReport(
            @Param("filterCriteria") Map params,
            @Param("rowBounds") RowBounds rowBounds,
            @Param("userId") Long userId
    );



}
