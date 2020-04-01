package org.openlmis.web.controller;

import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductPriceSchedule;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
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
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@Controller
public class MonitoringReportController extends BaseController {

    @Autowired
    private MonitoringReportService service;

    @RequestMapping(value = "/rest-api/monitoring-report/initiate/{zone}/{programId}.json", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'CREATE_MONITORING_REPORT')")
    public ResponseEntity<OpenLmisResponse> getMonitoringReport(@PathVariable("zone") Long zone,@PathVariable("programId") Long programId, HttpServletRequest request) {
        if (zone == null || zone == 0) return null;
        Long userId = this.loggedInUserId(request);

        MonitoringReport report = service.initiate(zone,programId,userId,null);
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
}
