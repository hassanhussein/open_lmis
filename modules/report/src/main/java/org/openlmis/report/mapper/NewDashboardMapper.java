package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.SelectProvider;
import org.openlmis.report.builder.*;
import org.openlmis.report.model.dto.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewDashboardMapper {
    @SelectProvider(type = DashboardReportingRateQueryBuilder.class, method = "getQuery")
    List<ReportingRate> getReportingRate(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardStockStatusQueryBuilder.class, method = "getQuery")
    List<StockStatus> getStockStaus(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardOrderFillRateBuilder.class, method = "getQuery")
    List<DashOrderFillRate> getItemFillRate(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardDailyStockStatusQueryBuilder.class, method = "getQuery")
    List<DashDailyStockStatus> getDailyStockStatus(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardCommodityStatusQueryBuilder.class, method = "getQuery")
    List<OSAndUsCommodity> getCommodityMostOSandUSList(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardCommodityStatusQueryBuilder.class, method = "getFacilityQuery")
    List<OSAndUsFacilityCommodity> getFacilityCommodityMostOSandUSList(Long zoneId, Long periodId, Long programId);
}
