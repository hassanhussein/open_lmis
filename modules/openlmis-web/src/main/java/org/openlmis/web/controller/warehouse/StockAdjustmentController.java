package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.vaccine.domain.wms.Adjustment;
import org.openlmis.vaccine.service.warehouse.AdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/adjustment")
public class StockAdjustmentController extends BaseController {
    @Autowired
    private FacilityService facilityService;
    @Autowired
    AdjustmentService adjustmentService;

    @RequestMapping(value = "save", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> save(@RequestBody Adjustment item, HttpServletRequest request) {
        //Long userId = loggedInUserId(request);
        long userId=307;
        item.setCreatedBy(userId);
        item.setModifiedBy(userId);
        Facility facility = facilityService.getHomeFacility(userId);
        long facilityID=0;
        try{
            facilityID=facility.getId();
        }catch (Exception e){

        }
        System.out.println("Done: u: "+userId);
        return OpenLmisResponse.response("adju",adjustmentService.save(item,loggedInUserId(request),facilityID));

    }
}
