package org.openlmis.web.controller;

import io.swagger.annotations.ApiOperation;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductPriceSchedule;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.ivdform.domain.reports.VaccineReport;
import org.openlmis.rnr.domain.MonitoringReport;
import org.openlmis.rnr.service.MonitoringReportService;
import org.openlmis.web.form.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


import java.util.List;

import static org.openlmis.core.web.OpenLmisResponse.response;
import static org.openlmis.core.web.OpenLmisResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@Controller
public class MonitoringReportController extends BaseController {

    @Autowired
    private MonitoringReportService service;

    @RequestMapping(value = "/rest-api/monitoring-report/initiate/{facilityId}/{programId}/{reportedDate}.json", method = POST, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'CREATE_MONITORING_REPORT')")
    public ResponseEntity<OpenLmisResponse> getMonitoringReport(@PathVariable("facilityId") Long facilityId,@PathVariable("programId") Long programId, @PathVariable("reportedDate") String reportedDate, HttpServletRequest request) {
        if (facilityId == null || facilityId == 0) return null;
        Long userId = this.loggedInUserId(request);

        MonitoringReport report = service.initiate(facilityId,programId,userId,reportedDate);
         if(report != null) {
             report = service.getReportById(report.getId());
         }

        return OpenLmisResponse.response("monitoringData", report);
    }


    @RequestMapping(value = "/rest-api/monitoring-report/get/{id}.json", method = RequestMethod.GET)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'CREATE_MONITORING_REPORT')")
    public ResponseEntity<OpenLmisResponse> getReport(@PathVariable Long id, HttpServletRequest request) {
        return response("report", service.getReportById(id));
    }

    @RequestMapping(value = "/rest-api/monitoring-report/district/{programId}.json", method = RequestMethod.GET)
    public ResponseEntity<OpenLmisResponse> getDistrictsBy(@PathVariable Long programId, HttpServletRequest request) {
        return response("districts", service.getDistrictBy(loggedInUserId(request), programId));
    }

    @RequestMapping(value = "/rest-api/monitoring-report/save/{id}.json", method = PUT, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'CREATE_MONITORING_REPORT')")
    public ResponseEntity<OpenLmisResponse> update(@RequestBody MonitoringReport report, @PathVariable(value = "id") Long id,
                                                   HttpServletRequest request) {
        try {
            Long userId = loggedInUserId(request);
            report.setId(id);
            report.setModifiedBy(userId);
            service.save(report);

        } catch (DataException e) {
            return OpenLmisResponse.error(e, BAD_REQUEST);
        }
        return success(messageService.message("Monitoring Form Successiful Updated", report.getId()));
    }


    @RequestMapping(value = {"/rest-api/monitoring-report/submit"}, method = RequestMethod.PUT)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'CREATE_MONITORING_REPORT')")
    public ResponseEntity<OpenLmisResponse> submit(@RequestBody MonitoringReport report, HttpServletRequest request) {
        service.submit(report, loggedInUserId(request));
        return OpenLmisResponse.response("report", report);
    }

    @RequestMapping(value = {"/rest-api/monitoring-report/pendingForApproval/{programId}.json"}, method = GET)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'APPROVE_MONITORING_REPORT')")
    public ResponseEntity<OpenLmisResponse> pendingForApproval(@PathVariable("programId") Long programId, HttpServletRequest request) {

        return OpenLmisResponse.response("report", service.pendingForApproval(programId, loggedInUserId(request)));
    }

    @RequestMapping(value = {"/rest-api/monitoring-report/approve"}, method = RequestMethod.PUT)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'APPROVE_MONITORING_REPORT')")
    public ResponseEntity<OpenLmisResponse> approve(@RequestBody MonitoringReport report, HttpServletRequest request) {
        service.approve(report, loggedInUserId(request));
        return OpenLmisResponse.response("report", report);
    }
}
