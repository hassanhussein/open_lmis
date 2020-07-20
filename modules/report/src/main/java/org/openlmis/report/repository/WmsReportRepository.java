package org.openlmis.report.repository;

import lombok.NoArgsConstructor;
import org.openlmis.report.mapper.LotsOnHandMapper;
import org.openlmis.report.mapper.VaccineStockStatusMapper;
import org.openlmis.report.model.wmsreport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class WmsReportRepository {
    private List<VaccineDistribution> vaccines;

    @Autowired
    LotsOnHandMapper lotsOnHandMapper;
    @Autowired
    VaccineStockStatusMapper vaccineStockStatusMapper;
    public List<StockCards> getReportListWithFullAttributes(Long facilityId){
        return lotsOnHandMapper.getListWithFullAttributes(facilityId);
    }

    public List<StockCard> getListStockProduct(Long warehouseId){
        return lotsOnHandMapper.getListStockProduct(warehouseId);
    }

    public  List<HashMap<String, Object>> getVarReportById(Long inspectionId){
        return lotsOnHandMapper.getListVarReport(inspectionId);

    }

    public List<StockCards> getListReports(Long productId,Long warehouseId){
        return lotsOnHandMapper.getListStockOnHand(productId,warehouseId);
    }

    public Facilities getFacilityDetails(Long facilityId){
        return lotsOnHandMapper.getFacilityDetails(facilityId);
    }

    public List<VaccineDistributionLineItem> getReportVaccine(Long facilityId){
        return vaccineStockStatusMapper.vaccineDistributionLineItemList(facilityId);
    }
    public List<VaccineDistribution> getReportVaccineDistribution(Long orderId){
        return vaccineStockStatusMapper.vaccineDistributionListByOrderId(orderId);
    }

    public List<VaccineDistributionLineItem> vaccineDistributionLineItemListByDistribution(Long distId){
        return vaccineStockStatusMapper.vaccineDistributionLineItemListByDistribution(distId);
    }

    public List<VaccineDistributionLots> vaccineDistributionLotList(Long distId){
        return vaccineStockStatusMapper.vaccineDistributionLots(distId);
    }

    public List<VaccineDistribution> getVaccineList() {
        return vaccines;
    }
    public List<VaccineDistribution> getVaccineDistributionByID(Long distId) {
        return vaccineStockStatusMapper.vaccineDistributionById(distId);
    }

}
