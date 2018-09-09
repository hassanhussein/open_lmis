package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
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
    @SelectProvider(type = ProductExpiryQueryBuilder.class, method = "getQuery")
    List<ProductExpiry> getExpiredProducts(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardRegularEmergencyTypeQueryBuilder.class, method = "getQuery")
    List<RnrRegularEmergencyType> getRnrWithTypeCount(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = ShipmentInterfaceQueryBuilder.class, method = "getQuery")
    @Results({@Result(property = "orderDate", column = "order_date")})
    List<ShipmentInterface> getInterfacesStatusReport(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardVitalStatusQueryBuilder.class, method = "getQuery")
    @Results(
            value ={ @Result(property = "model", column = "model"),
            @Result(property = "currentMonth", column = "current_month"),
            @Result(property = "lastThreeMonth", column = "last_three_month"),
            @Result(property = "lastSixMonth", column = "last_six_month"),
                    @Result(property = "total", column = "total")
})
    List<VitalStatusDto> getVitalStatuees(Long zoneId, Long periodId, Long programId);
    @SelectProvider(type = DashboardSupplyStatusQueryBuilder.class, method = "getQuery")
    @Results(
            value ={ @Result(property = "productCode", column = "productcode"),
                    @Result(property = "product", column = "product"),
                    @Result(property = "fillZero", column = "fill_0"),
                    @Result(property = "lessThan25", column = "less_25"),
                    @Result(property = "lessThan50", column = "less_50"),
                    @Result(property = "lessThan75", column = "less_75")
            })
    List<SupplyStatusDto> getProductSupplyStatus(Long zoneId, Long periodId, Long programId);
}
