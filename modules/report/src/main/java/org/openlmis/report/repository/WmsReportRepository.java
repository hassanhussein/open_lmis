package org.openlmis.report.repository;

import lombok.NoArgsConstructor;
import org.openlmis.report.mapper.LotsOnHandMapper;
import org.openlmis.report.model.wmsreport.Facilities;
import org.openlmis.report.model.wmsreport.StockCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class WmsReportRepository {
    @Autowired
    LotsOnHandMapper lotsOnHandMapper;
    public List<StockCards> getReportListWithFullAttributes(Long facilityId){
        return lotsOnHandMapper.getListWithFullAttributes(facilityId);
    }
    public List<StockCards> getListReports(Long facilityId){
        return lotsOnHandMapper.getListWithFullAttributes(facilityId);
    }

    public Facilities getFacilityDetails(Long facilityId){
        return lotsOnHandMapper.getFacilityDetails(facilityId);
    }
}
