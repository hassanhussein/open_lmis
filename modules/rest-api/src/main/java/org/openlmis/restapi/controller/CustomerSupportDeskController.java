package org.openlmis.restapi.controller;


import com.wordnik.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.User;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.UserService;
import org.openlmis.restapi.domain.SDIssue;
import org.openlmis.restapi.service.RestSDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
@NoArgsConstructor
@Api(value = "Customer Support Desk", description = "Customer Support Desk API interact with eLMIS Tanzania JIRA", position = 13)
public class CustomerSupportDeskController extends BaseController {

    @Autowired
    RestSDService restSDService;

    @Autowired
    UserService userService;

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
}
