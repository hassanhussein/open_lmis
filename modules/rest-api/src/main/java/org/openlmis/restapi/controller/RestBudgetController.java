package org.openlmis.restapi.controller;

import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.pod.domain.OrderPOD;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.restapi.service.RestBudgetService;
import org.openlmis.restapi.service.RestPODService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * This controller is responsible for handling API endpoint to update budget details for a specified
 * facility id, along with other details(attributes of Facility Budget).
 */

@Controller
@NoArgsConstructor
@Api(value="Budget", description = "Allows Submission of Facility Level Budget", position = 1)

public class RestBudgetController extends BaseController{

    @Autowired
    private RestBudgetService restBudgetService;


    @RequestMapping(value = "/rest-api/budget/{facilityCode}/budget", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> saveBudget(@RequestBody BudgetDTO budgetDTO, @PathVariable String facilityCode, Principal principal) {
        try {
            restBudgetService.updateBudget(budgetDTO, loggedInUserId(principal),facilityCode);
            return RestResponse.success("message.success.budget.updated");
        } catch (DataException e) {
            return RestResponse.error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

}
