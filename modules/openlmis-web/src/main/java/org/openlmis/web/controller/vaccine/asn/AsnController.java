package org.openlmis.web.controller.vaccine.asn;

import lombok.NoArgsConstructor;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.restapi.controller.BaseController;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.asn.Asn;
import org.openlmis.vaccine.service.asn.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/asn")
public class AsnController extends BaseController {

    @Autowired
    AsnService asnService;
    @Autowired
    AsnLineItemService asnLineItemService;
    @Autowired
    AsnLotService asnLotService;
    @Autowired
    PurchaseDocumentService purchaseDocumentService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    PortService portService;
    @RequestMapping(method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody Asn asn, Principal principal) {
        try {
            Long userId = loggedInUserId(principal);
            asn.setCreatedBy(userId);
            asn.setModifiedBy(userId);
            asnService.save(asn, userId);
            return success("message.success.asn.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "document-types", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getDocumentTypes() {
        return OpenLmisResponse.response("documents", documentTypeService.getAll());
    }
    @RequestMapping(value = "ports", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getPorts() {
        return OpenLmisResponse.response("ports", portService.getAll());
    }
    @RequestMapping(method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> retriveAllAsns() {
        return OpenLmisResponse.response("asns", asnService.getAll());
    }
    @RequestMapping(value = "{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getById(@PathVariable Long id) {
        return OpenLmisResponse.response("asn", asnService.getById(id));
    }
    @RequestMapping(value = "{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Asn asn, @PathVariable(value = "id") Long id,Principal principal) {

       try{
        asn.setId(id);
        asn.setModifiedBy(loggedInUserId(principal));
        asnService.save(asn, loggedInUserId(principal));
        return success("message.success.asn.updated");

    } catch (DataException e) {
        return error(e.getOpenLmisMessage(), BAD_REQUEST);
    }
    }

}
