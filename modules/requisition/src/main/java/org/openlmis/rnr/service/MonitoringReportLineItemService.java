package org.openlmis.rnr.service;

import org.openlmis.rnr.domain.MonitoringReportLineItem;
import org.openlmis.rnr.repository.MonitoringReportLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitoringReportLineItemService {

    @Autowired
    private MonitoringReportLineItemRepository reportLineItemRepository;

    public void save(MonitoringReportLineItem item){
        if(item.getId() == null){
            reportLineItemRepository.insert(item);
        }else {
            reportLineItemRepository.update(item);
        }

    }


}
