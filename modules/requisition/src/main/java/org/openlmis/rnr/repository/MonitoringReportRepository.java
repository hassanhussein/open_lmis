package org.openlmis.rnr.repository;

import org.openlmis.rnr.domain.MonitoringReport;
import org.openlmis.rnr.domain.MonitoringReportLineItem;
import org.openlmis.rnr.repository.mapper.MonitoringMapper;
import org.openlmis.rnr.service.MonitoringReportLineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MonitoringReportRepository {

    @Autowired
    private MonitoringMapper mapper;

    @Autowired
    private MonitoringReportLineItemService itemService;


    private Integer insert(MonitoringReport report){
        return mapper.insert(report);
    }

    private void update(MonitoringReport report){

        mapper.update(report);
    }

    public MonitoringReport getBy(Long zoneId, Long programId, Long userId, String reportedDate) {
        return mapper.getBy(zoneId,programId, userId,reportedDate);
    }

    public void save(MonitoringReport report) {

        if(report.getId() == null ) {
            mapper.insert(report);
        } else {
            mapper.update(report);
        }

        saveDetails(report, report.getLineItems());

    }

    private void saveDetails(MonitoringReport report, List<MonitoringReportLineItem> lineItems) {

        if (!lineItems.isEmpty()) {

            for (MonitoringReportLineItem item : lineItems) {
                item.setReportId(report.getId());
                if(item.getProductCategories() != null) {
                    item.setProductCategoryId(item.getProductCategories().getId());
                }
                itemService.save(item);
            }
    }
    }

    public MonitoringReport getReportById(Long id){
        return mapper.getReportById(id);

    }
}
