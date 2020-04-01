package org.openlmis.rnr.repository;

import org.openlmis.rnr.domain.MonitoringReportLineItem;
import org.openlmis.rnr.repository.mapper.MonitoringLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitoringReportLineItemRepository {

    @Autowired
    private MonitoringLineItemMapper mapper;

    public Integer insert(MonitoringReportLineItem lineItem){
        return  mapper.insert(lineItem);
    }

    public void  update(MonitoringReportLineItem lineItem){
        mapper.update(lineItem);
    }
}
