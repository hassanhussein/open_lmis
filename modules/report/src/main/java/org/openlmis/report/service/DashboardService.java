package org.openlmis.report.service;

import org.openlmis.report.mapper.NewDashboardMapper;
import org.openlmis.report.mapper.lookup.DashboardMapper;
import org.openlmis.report.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public class DashboardService {
    @Autowired
    private NewDashboardMapper dashboardMapper;
    public List<ReportingRate>  getReportingRate(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getReportingRate(zoneId,periodId,programId);
    }
    public List<StockStatus>  getStockStaus(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getStockStaus(zoneId,periodId,programId);
    }
    public List<DashOrderFillRate>  getItemFillRate(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getItemFillRate(zoneId,periodId,programId);
    }
    public List<DashDailyStockStatus>  getDailyStockStatus(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getDailyStockStatus(zoneId,periodId,programId);
    }
    public List<OSAndUsCommodity>  getCommodityMostOSandUSList(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getCommodityMostOSandUSList(zoneId,periodId,programId);
    }
    public List<OSAndUsFacilityCommodity>  getFacilityCommodityMostOSandUSList(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getFacilityCommodityMostOSandUSList(zoneId,periodId,programId);
    }
    public List<ProductExpiry>  getExpiredProducts(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getExpiredProducts(zoneId,periodId,programId);
    }
    public List<RnrRegularEmergencyType>  getRnrWithTypeCount(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getRnrWithTypeCount(zoneId,periodId,programId);
    }
    public List<ShipmentInterface>  getInterfacesStatusReport(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getInterfacesStatusReport(zoneId,periodId,programId);
    }
    public List<VitalStatusDto>  getVitalStatuees(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getVitalStatuees(zoneId,periodId,programId);
    }
    public List<SupplyStatusDto>  getProductSupplyStatus(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getProductSupplyStatus(zoneId,periodId,programId);
    }
    public List<RnRTimeLine>  getRnrTimeLine(Long zoneId, Long periodId, Long programId) {
        return  dashboardMapper.getRnrTimeLine(zoneId,periodId,programId);
    }
    public List<UserDto>  getUsersInThreeMonths() {
        return  dashboardMapper.getUsersInThreeMonths();
    }

    public List<Map<String, Object>> getAggregateExpiry(Long program) {
        return dashboardMapper.getAggregateExpiry(program);
    }
}
