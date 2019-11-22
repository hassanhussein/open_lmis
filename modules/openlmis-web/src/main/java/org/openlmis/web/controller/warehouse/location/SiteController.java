package org.openlmis.web.controller.warehouse.location;

import lombok.NoArgsConstructor;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.service.warehouse.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;

import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class SiteController extends BaseController {

    @Autowired
    private SiteService service;

    @RequestMapping(value = "site", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody Site site,  HttpServletRequest request) {
        try {
            Long userId = loggedInUserId(request);
            site.setCreatedBy(userId);
            site.setModifiedBy(userId);
            service.save(site, userId);
            return success("message.success.site.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }



    @RequestMapping(value = "site/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Site site, @PathVariable(value = "id") Long id, HttpServletRequest request) {

        try{
            site.setId(id);
            site.setModifiedBy(loggedInUserId(request));
            service.save(site, loggedInUserId(request));
            return success("message.success.warehouse.updated");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "site/{regionId}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllSites(@PathVariable Long regionId, HttpServletRequest request) {
        return OpenLmisResponse.response("sites", service.getAll(regionId));
    }


}
