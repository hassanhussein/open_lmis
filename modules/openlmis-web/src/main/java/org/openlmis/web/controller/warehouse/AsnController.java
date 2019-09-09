package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.SupplyPartnerService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.ivdform.domain.Manufacturer;
import org.openlmis.ivdform.service.ManufacturerService;
import org.openlmis.restapi.controller.BaseController;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.DocumentType;
import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.service.warehouse.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
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
    @Autowired
    ManufacturerService manufacturerService;
    @Autowired
    SupplyPartnerService supplyPartnerService;
    @RequestMapping(value = "asn", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody Asn asn, Principal principal) {
        try {
            Long userId = loggedInUserId(principal);
            asn.setCreatedBy(userId);
            asn.setModifiedBy(userId);
            asnService.save(asn, userId);
            return success("message.success.warehouse.created");

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
    @RequestMapping(value = "all",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> retriveAllAsns() {
        return OpenLmisResponse.response("asns", asnService.getAll());
    }
    @RequestMapping(value = "asn/template",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAsnLookupTemplate() {

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
    @RequestMapping(value = "asn", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAsnByPagination(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                               @RequestParam(value = "column") String column,
                                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(asnService.getTotalSearchResultCount(searchParam, column));
        List<Asn> asns = asnService.searchBy(searchParam, column, pagination);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("asns", asns);
        response.getBody().addData("pagination", pagination);
        return response;
    }
    @RequestMapping(value = "asn/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getById(@PathVariable Long id) {
        return OpenLmisResponse.response("asn", asnService.getById(id));
    }
    @RequestMapping(value = "asn/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Asn asn, @PathVariable(value = "id") Long id,Principal principal) {

       try{
        asn.setId(id);
        asn.setModifiedBy(loggedInUserId(principal));
        asnService.save(asn, loggedInUserId(principal));
        return success("message.success.warehouse.updated");

    } catch (DataException e) {
        return error(e.getOpenLmisMessage(), BAD_REQUEST);
    }
    }

    @RequestMapping(value = "/asn/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteLocation(@PathVariable("id") Long id) {

        try {
            asnService.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            return OpenLmisResponse.error("asn.data.already.in.use", HttpStatus.BAD_REQUEST);
        }

        return OpenLmisResponse.success("message.asn.deleted.success");
    }

}
