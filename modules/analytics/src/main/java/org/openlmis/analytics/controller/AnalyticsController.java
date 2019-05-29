package org.openlmis.analytics.controller;

import lombok.NoArgsConstructor;
import org.openlmis.analytics.service.DashboardService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/api/dashboard")
public class AnalyticsController extends BaseController {

    @Autowired
    private DashboardService service;

    @RequestMapping(value = "/requisition-report", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> requisitionRepor() {
        return OpenLmisResponse.response("rnr_list", service.getAllUsers());
    }


}