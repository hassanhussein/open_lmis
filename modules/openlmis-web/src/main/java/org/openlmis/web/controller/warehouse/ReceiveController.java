package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.MessageService;
import org.openlmis.core.service.SupplyPartnerService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.ivdform.domain.Manufacturer;
import org.openlmis.ivdform.service.ManufacturerService;
import org.openlmis.restapi.controller.BaseController;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.DocumentType;
import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.domain.wms.Receive;
import org.openlmis.vaccine.service.warehouse.DocumentTypeService;
import org.openlmis.vaccine.service.warehouse.PortService;
import org.openlmis.vaccine.service.warehouse.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class ReceiveController extends BaseController {

    @Autowired
    private ReceiveService service;
    @Autowired
    MessageService messageService;

    @Autowired
    DocumentTypeService documentTypeService;

    @Autowired
    PortService portService;

    @Autowired
    ManufacturerService manufacturerService;
    @Autowired
    SupplyPartnerService supplyPartnerService;


    public static final String ERROR = "error";
    public static final String SUCESS = "success";


    @RequestMapping(value = "receive", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody Receive receive, Principal principal) {
        try {
            Long userId = loggedInUserId(principal);
            receive.setCreatedBy(userId);
            receive.setModifiedBy(userId);
            service.save(receive, userId);
            return success("message.success.warehouse.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "receive/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Receive receive, @PathVariable(value = "id") Long id,Principal principal) {

        try{
            receive.setId(id);
            receive.setModifiedBy(loggedInUserId(principal));
            service.save(receive, loggedInUserId(principal));
            return success("message.success.warehouse.updated");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "all",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> retriveAllReceive() {
        return OpenLmisResponse.response("receive", service.getAll());
    }

    @RequestMapping(value = "receive/template",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getReceiveLookupTemplate() {

        List<DocumentType> documentTypes = documentTypeService.getAll();
        List<Port> ports = portService.getAll();
        List<Manufacturer> manufacturers = manufacturerService.getAll();
        List<SupplyPartner> supplyPartners = supplyPartnerService.getAll();
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("document-types", documentTypes);
        response.getBody().addData("ports", ports);
        response.getBody().addData("manufactures", manufacturers);
        response.getBody().addData("suppliers", supplyPartners);
        return response;
    }

    @RequestMapping(value = "receive/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getById(@PathVariable Long id) {
        return OpenLmisResponse.response("receive", service.getById(id));
    }

}
