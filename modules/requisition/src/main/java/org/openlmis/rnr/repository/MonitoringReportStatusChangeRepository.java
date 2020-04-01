package org.openlmis.rnr.repository;

import org.openlmis.rnr.domain.ReportStatusChange;
import org.openlmis.rnr.repository.mapper.MonitoringReportStatusChangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitoringReportStatusChangeRepository {

    @Autowired
    private MonitoringReportStatusChangeMapper mapper;

    public Integer insert(ReportStatusChange change){
       return   mapper.insert(change);
    }

}
