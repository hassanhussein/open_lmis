package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.LocationService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.stockmanagement.domain.StockCardEntryKV;
import org.openlmis.vaccine.domain.wms.Adjustment;
import org.openlmis.vaccine.dto.LocationDTO;
import org.openlmis.vaccine.service.warehouse.AdjustmentService;
import org.openlmis.vaccine.service.warehouse.WmsLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/adjustment")
public class StockAdjustmentController extends BaseController {
    @Autowired
    private FacilityService facilityService;
    @Autowired
    AdjustmentService adjustmentService;
    @Autowired
    WmsLocationService wmsLocationService;

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

        String transfer_logs=null;
        Long tobinLocation=item.getToBinId();

        try{
            LocationDTO locationDTO= wmsLocationService.getByLocationId(item.getLocationid());
            transfer_logs=locationDTO.getName();

            if(tobinLocation != null && tobinLocation != 0){
                LocationDTO locationDTOto= wmsLocationService.getByLocationId(tobinLocation);
                transfer_logs=transfer_logs+"-"+locationDTOto.getName();
            }

            item.setTransferLogs(transfer_logs);
            //System.out.println("h: "+locationDTO.getName());

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Done: u: "+tobinLocation);
        Object results=adjustmentService.save(item,loggedInUserId(request),facilityID);
        Boolean isTransfer=item.getIsTransfer();
        String getReason=item.getReason();
        if(getReason.equals("VVM Change")){
            Long toVvmId=item.getToVvmId();
            if(toVvmId>2){
                if (tobinLocation != null && tobinLocation != 0) {
                    item.setLocationid(tobinLocation);
                    item.setVvmId(toVvmId);
                    item.setType("CREDIT");
                    item.setId(null);
                    item.setQuantity(Math.abs(item.getQuantity()));

                   // System.out.println("Passed: " + item.toString());

                    adjustmentService.save(item, loggedInUserId(request), facilityID);
                }
            }else {
                item.setVvmId(toVvmId);
                item.setType("CREDIT");
                item.setId(null);
                item.setQuantity(Math.abs(item.getQuantity()));

                // System.out.println("Passed: " + item.toString());

                adjustmentService.save(item, loggedInUserId(request), facilityID);
            }
        }else {

            if (tobinLocation != null && tobinLocation != 0 && isTransfer) {
                item.setLocationid(tobinLocation);
                item.setType("CREDIT");
                item.setId(null);
                item.setQuantity(Math.abs(item.getQuantity()));

                ///System.out.println("Passed: " + item.toString());

                adjustmentService.save(item, loggedInUserId(request), facilityID);
            }



        }

        return OpenLmisResponse.response("adju",results);

    }
}
