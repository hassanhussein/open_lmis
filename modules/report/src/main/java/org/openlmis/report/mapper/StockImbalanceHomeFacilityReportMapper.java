package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.StockImbalanceHomeFacilityQueryBuilder;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.params.StockImbalanceReportParam;
import org.openlmis.report.model.report.StockImbalanceReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StockImbalanceHomeFacilityReportMapper {
    @SelectProvider(type= StockImbalanceHomeFacilityQueryBuilder.class, method="getReportQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    public List<StockImbalanceReport> getReport(@Param("filterCriteria") ReportParameter filterCriteria,
                                                @Param("SortCriteria") Map<String, String[]> sortCriteria ,
                                                @Param("RowBounds") RowBounds rowBounds,
                                                @Param("userId") Long userId );

    @SelectProvider(type= StockImbalanceHomeFacilityQueryBuilder.class, method="getTotalNumberOfRowsQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    StockImbalanceReport getTotalFilteredSortedPagedStockImbalanceReport(
            @Param("filterCriteria") StockImbalanceReportParam filterCriteria,
            @Param("SortCriteria") Map<String, String[]> sortCriteria ,
            @Param("userId")Long userId
    );

}
