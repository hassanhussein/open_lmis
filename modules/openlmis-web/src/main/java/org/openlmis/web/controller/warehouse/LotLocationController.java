package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.vaccine.domain.wms.dto.PutAwayLineItemDTO;
import org.openlmis.vaccine.service.warehouse.LotOnHandLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/lotLocation")
public class LotLocationController extends BaseController {

    @Autowired
    private LotOnHandLocationService lotOnHandLocationService;

    @RequestMapping(value = "inspection/put-away", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> savePutAwayDetails(@RequestBody List<PutAwayLineItemDTO> item, HttpServletRequest request) {

        return OpenLmisResponse.response("aways",lotOnHandLocationService.savePutAwayDetails(item,loggedInUserId(request)));
    }

}
