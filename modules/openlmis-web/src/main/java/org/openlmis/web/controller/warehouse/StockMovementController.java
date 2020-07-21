package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.vaccine.domain.wms.Adjustment;
import org.openlmis.vaccine.domain.wms.Transfer;

import org.openlmis.vaccine.service.warehouse.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/transfer")
public class StockMovementController extends BaseController {

    @Autowired
    private TransferService transferService;
    @Autowired
    private FacilityService facilityService;

    @RequestMapping(value = "save", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> save(@RequestBody Transfer item, HttpServletRequest request) {
        Long userId = loggedInUserId(request);
        item.setCreatedBy(userId);
        item.setModifiedBy(userId);
        Facility facility = facilityService.getHomeFacility(userId);
        return OpenLmisResponse.response("trans",transferService.save(item,loggedInUserId(request),facility.getId()));

    }


    /*@RequestMapping(value = "update", method = PUT, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> update(@RequestBody Transfer item, HttpServletRequest request) {
        transferService.update(item,loggedInUserId(request));
        return OpenLmisResponse.response("trans","updated");
    }*/

    @RequestMapping(value = "SearchBy", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getBy(@RequestParam(value = "searchParam") String searchParam) {
        return OpenLmisResponse.response("transfers",transferService.search(searchParam));
    }

    @RequestMapping(value = "all-transfers", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAll() {
        return OpenLmisResponse.response("transfers",transferService.getAll());
    }

   @RequestMapping(value = "all-transfer-reasons", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllTransferReasons() {
        return OpenLmisResponse.response("reasons",transferService.getTransferReasons());
    }


    @RequestMapping(value = "/get-current-stock", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getCurrentStockOnHand(HttpServletRequest request) {

        Long userId = loggedInUserId(request);
        Facility facility = facilityService.getHomeFacility(userId);
        return OpenLmisResponse.response("stocks",transferService.getCurrentStockOnHand(userId,facility.getId()));
    }





}
