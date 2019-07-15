package org.openlmis.web.controller;


import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.restapi.controller.BaseController;
import org.openlmis.rnr.service.PatientColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class PatientTemplateController extends BaseController {


    @Autowired
    PatientColumnService service;



    @RequestMapping(value = "/programId/{programId}/programPatientTemplate", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_REQUISITION')")
    public ResponseEntity<OpenLmisResponse> getProgramPatientTemplate(@PathVariable Long programId) {
        return OpenLmisResponse.response("template", service.getPatientTemplateByProgramId(programId));
    }
}
