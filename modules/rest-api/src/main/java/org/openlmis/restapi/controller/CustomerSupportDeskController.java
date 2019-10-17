package org.openlmis.restapi.controller;


import com.wordnik.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.User;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.UserService;
import org.openlmis.restapi.domain.SDIssue;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.restapi.service.RestRequisitionService;
import org.openlmis.restapi.service.RestSDService;
import org.openlmis.rnr.domain.Rnr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.response;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
@NoArgsConstructor
@Api(value = "Customer Support Desk", description = "Customer Support Desk API interact with eLMIS Tanzania JIRA", position = 13)
public class CustomerSupportDeskController extends BaseController {

    @Autowired
    RestSDService restSDService;

    @Autowired
    UserService userService;

    @Autowired
    RestRequisitionService restRequisitionService;

    public static final String RNR = "requisition";

    @ResponseBody
    @RequestMapping(value = "/rest-api/support-desk/getArticlesBySearchKeyword", method = GET, headers = BaseController.ACCEPT_JSON)
    public String getArticlesBySearchKeyword(@RequestParam String query) {
        try {
            return restSDService.getArticlesBySearchKeyword(query);
        } catch (DataException de) {
            return de.getOpenLmisMessage().toString();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/rest-api/support-desk/reportIssueOnSD", method = GET, headers = BaseController.ACCEPT_JSON)
    public String reportIssueOnSD(@RequestParam String summary, @RequestParam String description, @RequestParam Long reporterId) {
        try {

            User user = userService.getById(reporterId);
            SDIssue sdIssue = new SDIssue();

            sdIssue.setSummary(summary);
            sdIssue.setDescription(description);
            sdIssue.setRaiseOnBehalfOf(user.getEmail());
            return restSDService.createIssue(sdIssue);
        } catch (DataException de) {
            return de.getOpenLmisMessage().toString();
        }
    }


    @RequestMapping(value = "/rest-api/support-desk/getLatestRequisitionByFacilityCode", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getRequisitionsByFacilityAndProgram(@RequestParam(value = "facilityCode") String facilityCode, @RequestParam(value = "programCode") String programCode, Principal principal) {
        try {
            Rnr requisition = restRequisitionService.getRequisitionStatusByFacilityAndProgram(facilityCode, programCode);
            ResponseEntity<RestResponse> response = response(RNR, requisition);
            return response;
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/rest-api/support-desk/addSubscribers", method = GET, headers =ACCEPT_JSON)
    public ResponseEntity<RestResponse> addNotificationSubscribers (@RequestParam(value = "chatId") String chatId, @RequestParam(value = "rnrId") Integer rnrId, @RequestParam(value = "label") String label)
    {
        try {
            restSDService.addNotificationSubscribers(chatId, rnrId,label, "TELEGRAM");
            return RestResponse.success("message.success.subscriber.added");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }
}
