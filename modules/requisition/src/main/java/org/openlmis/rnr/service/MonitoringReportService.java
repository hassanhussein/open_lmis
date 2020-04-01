package org.openlmis.rnr.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.dto.SupervisoryNodeDTO;
import org.openlmis.core.service.*;
import org.openlmis.rnr.domain.MonitoringReport;
import org.openlmis.rnr.domain.ReportStatusChange;
import org.openlmis.rnr.domain.RnrStatus;
import org.openlmis.rnr.repository.MonitoringReportRepository;
import org.openlmis.rnr.repository.MonitoringReportStatusChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.openlmis.core.domain.RightName.CREATE_MONITORING_FORM;


@Service
public class MonitoringReportService {

    @Autowired
    private MonitoringReportRepository repository;

    @Autowired
    private MonitoringReportStatusChangeRepository reportStatusChangeRepository;

    @Autowired
    private SupervisoryNodeService supervisoryNodeService;

    @Autowired
    private GeographicZoneService geographicZoneService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private ProgramProductService programProductService;

    @Autowired
    private ProgramService programService;

   @Transactional
   public MonitoringReport initiate(Long zoneId,Long programId, Long userId, String reportedDate) {

       if(reportedDate == null) {

           reportedDate = new Date().toString();
       }

       SupervisoryNodeDTO supervisoryNode = supervisoryNodeService.getByuserAndRightName(userId,programId,CREATE_MONITORING_FORM);

       if(supervisoryNode != null) {
           Facility facility  = facilityService.getById(supervisoryNode.getFacilityId());
           zoneId = facility.getGeographicZone().getId();
       }

       MonitoringReport report = repository.getBy(zoneId,programId, userId, reportedDate);

       if(report == null && supervisoryNode !=null) {

        report = createNew(zoneId,programId,supervisoryNode.getId(),reportedDate,userId);

       repository.save(report);
       ReportStatusChange change = new ReportStatusChange(report, RnrStatus.INITIATED,userId);
       reportStatusChangeRepository.insert(change);
       }

       return report;
   }

   private MonitoringReport createNew(Long zoneId, Long programId,Long supervisoryNodeId, String reportedDate, Long userId) {

       List<ProgramProduct> programProduct = programProductService.getByProgram(programService.getById(programId));

       MonitoringReport report = new MonitoringReport();
       Date date = new Date();
       SimpleDateFormat form = new SimpleDateFormat("MM-dd-YYYY");
       report.setDistrictId(zoneId);
       report.setProgramId(programId);
       report.setNameOfHidTu(null);
       report.setNumberOfHidTu(0L);
       report.setNumberOfStaff(null);
       report.setNumberOfCumulativeCases(0);
       report.setPatientOnTreatment(null);
       report.setStatus("INITIATED");
       report.setReportedDate(new Date());
       report.setCreatedBy(userId);
       report.setModifiedBy(userId);
       report.setSupervisoryNodeId(supervisoryNodeId);

       report.initializeLineItems(programProduct,null, false);

       return report;

   }

   public MonitoringReport getReportById(Long id) {
       return repository.getReportById(id);
   }

   public GeographicZone getDistrictBy(Long userId,Long programId) {
       Long zoneId = 0L;
       SupervisoryNodeDTO supervisoryNode = supervisoryNodeService.getByuserAndRightName(userId,programId,CREATE_MONITORING_FORM);
       if(supervisoryNode != null) {
           Facility facility  = facilityService.getById(supervisoryNode.getFacilityId());
           zoneId = facility.getGeographicZone().getId();
       }
       return geographicZoneService.getById(zoneId);
   }

    public void save(MonitoringReport report) {

        repository.save(report);
        if(report.getStatus().equalsIgnoreCase("SUBMITTED")){
            ReportStatusChange change = new ReportStatusChange(report, RnrStatus.SUBMITTED,report.getModifiedBy());
            reportStatusChangeRepository.insert(change);
        }
    }
}
