package org.openlmis.web.controller.warehouse.location;

import lombok.NoArgsConstructor;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.restapi.controller.BaseController;
import org.openlmis.vaccine.service.warehouse.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class ZoneController extends BaseController {

    @Autowired
    private ZoneService service;

    @RequestMapping(value = "zone", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllSites(HttpServletRequest request) {
        return OpenLmisResponse.response("zones", service.getAll());
    }


}
