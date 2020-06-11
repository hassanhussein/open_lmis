package org.openlmis.report.repository;

import lombok.NoArgsConstructor;
import org.openlmis.report.mapper.LotsOnHandMapper;
import org.openlmis.report.mapper.VaccineStockStatusMapper;
import org.openlmis.report.model.wmsreport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<StockCards> getListReports(Long facilityId){
        return lotsOnHandMapper.getListWithFullAttributes(facilityId);
    }

    public Facilities getFacilityDetails(Long facilityId){
        return lotsOnHandMapper.getFacilityDetails(facilityId);
    }

    public List<VaccineDistributionLineItem> getReportVaccine(){
        return vaccineStockStatusMapper.vaccineDistributionLineItemList();
    }
    public List<VaccineDistribution> getReportVaccineDistribution(){
        return vaccineStockStatusMapper.vaccineDistributionList();
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
