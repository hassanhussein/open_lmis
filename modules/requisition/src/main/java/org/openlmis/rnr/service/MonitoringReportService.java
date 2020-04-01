package org.openlmis.rnr.service;

import org.openlmis.core.domain.*;
import org.openlmis.core.dto.SupervisoryNodeDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.helper.CommaSeparator;
import org.openlmis.core.service.*;
import org.openlmis.rnr.domain.MonitoringReport;
import org.openlmis.rnr.domain.ReportStatusChange;
import org.openlmis.rnr.domain.RnrStatus;
import org.openlmis.rnr.dto.MonitoringReportDTO;
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

    @Autowired
    CommaSeparator commaSeparator;

   @Transactional
   public MonitoringReport initiate(Long facilityId,Long programId, Long userId, String reportedDate) {

       MonitoringReport report = repository.getReportByProgramAndFacility(facilityId, programId);

       if(report != null){
           return report;
       }
       validateInitiate(facilityId,programId,reportedDate);

       //SupervisoryNodeDTO supervisoryNode = supervisoryNodeService.getByuserAndRightName(userId,programId,CREATE_MONITORING_FORM);

       report = createNew(facilityId,programId,reportedDate,userId);

       repository.save(report);
       ReportStatusChange change = new ReportStatusChange(report, RnrStatus.INITIATED,userId);
       reportStatusChangeRepository.insert(change);

       return report;
   }

    private void validateInitiate(Long facilityId, Long programId, String reportDate) {

       MonitoringReport draftReport = repository.getDraftReport(facilityId, programId);

        if (draftReport != null) {
            throw new DataException("error.facility.has.pending.draft");
        }

        MonitoringReport submittedReport = repository.getAlreadySubmittedReport(facilityId, programId,reportDate);

        if(submittedReport != null) {

            throw new DataException("The report for this Date has been submitted");
        }

    }

   private MonitoringReport createNew(Long facilityId, Long programId, String reportedDate, Long userId) {
       MonitoringReport report;

       List<ProgramProduct> programProduct = programProductService.getActiveByProgram(programId);
       Facility facility = facilityService.getById(facilityId);
       Program program = programService.getById(programId);
       SupervisoryNode supervisoryNode = supervisoryNodeService.getFor(facility,program);

       //TODO
       MonitoringReport previousReport = repository.getPreviousReport(facilityId, programId, reportedDate);

       report = new MonitoringReport();
       Date date = new Date();
       SimpleDateFormat form = new SimpleDateFormat("MM-dd-YYYY");
       report.setFacilityId(facilityId);
       report.setProgramId(programId);
       report.setNameOfHidTu(facility.getName());
       report.setNumberOfHidTu(0L);
       report.setNumberOfStaff(null);
       report.setNumberOfCumulativeCases((previousReport != null)?previousReport.getPatientOnTreatment():0);
       report.setPatientOnTreatment(null);
       report.setStatus("INITIATED");
       report.setReportedDate(new Date());
       report.setCreatedBy(userId);
       report.setModifiedBy(userId);
       report.setSupervisoryNodeId(supervisoryNode.getId());

       report.initializeLineItems(programProduct,previousReport);

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
    }

    public void submit(MonitoringReport report, Long userId) {
        report.setStatus("SUBMITTED");
        report.setModifiedBy(userId);
      //  MonitoringReport reportFromDb = getReportFromDbForUpdate(report);
        repository.save(report);
        ReportStatusChange change = new ReportStatusChange(report, RnrStatus.SUBMITTED,report.getModifiedBy());
        reportStatusChangeRepository.insert(change);

    }

    private MonitoringReport getReportFromDbForUpdate(MonitoringReport report) {
        MonitoringReport reportFromDb = repository.getReportById(report.getId());
        if ("APPROVED".equals(reportFromDb.getStatus()) || "SUBMITTED".equals(reportFromDb.getStatus())) {
            throw new RuntimeException("Report arleady Submitted");
        }
        return reportFromDb;
    }

    public List<MonitoringReportDTO> pendingForApproval(Long programId, Long userId) {
       String facilityIds = commaSeparator.commaSeparateIds(facilityService.getUserSupervisedFacilities(userId, programId, "APPROVE_MONITORING_REPORT"));
       return repository.pendingForApproval(facilityIds);
    }

    public void approve(MonitoringReport report, Long userId) {

        report.setStatus("APPROVED");
        report.setModifiedBy(userId);
        repository.save(report);
        ReportStatusChange change = new ReportStatusChange(report, RnrStatus.APPROVED,report.getModifiedBy());
        reportStatusChangeRepository.insert(change);

    }
}
