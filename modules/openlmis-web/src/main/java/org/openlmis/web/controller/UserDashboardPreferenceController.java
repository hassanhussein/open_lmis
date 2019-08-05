package org.openlmis.web.controller;


import lombok.NoArgsConstructor;
import org.openlmis.core.domain.UserDashboardReference;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.logging.ApplicationLogger;
import org.openlmis.core.repository.UserDashboardPreferenceRepository;
import org.openlmis.core.service.UserDashboardReferenceService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.openlmis.core.web.OpenLmisResponse.error;
import static org.openlmis.core.web.OpenLmisResponse.success;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/dashboard")
public class UserDashboardPreferenceController extends BaseController {
    public static final String UNEXPECTED_EXCEPTION = "unexpected.exception";
    public static final String FORBIDDEN_EXCEPTION = "error.authorisation";
    public static final String ACCEPT_JSON = "Accept=application/json";
    public static final String ACCEPT_PDF = "Accept=application/pdf";
    public static final String ACCEPT_CSV = "Accept=*/*";
    public static final String ACCEPT_XML = "Accept=application/xml";
    public static final String PREFERENCES = "preferences";
    public static final String PREFERENCE = "preference";
    private static Logger logger = LoggerFactory.getLogger(UserDashboardPreferenceController.class);
    @Autowired
    UserDashboardReferenceService service;

    @RequestMapping(value = "/dashboard-preferences", method = POST, headers = ACCEPT_JSON)

    public ResponseEntity<OpenLmisResponse> addPreference(@RequestBody  List<UserDashboardReference> dashboardReferencesList, HttpServletRequest request) {
        if (dashboardReferencesList != null && !dashboardReferencesList.isEmpty()) {
            try {
                dashboardReferencesList.stream().forEach((dashboardReference -> {
                    dashboardReference.setCreatedBy(loggedInUserId(request));
                    dashboardReference.setModifiedBy(loggedInUserId(request));

                    service.addPreference(dashboardReference);

                }));
                return success(messageService.message("message.dashboard.dashlet.preference.saved.success"));

            } catch (DataException e) {
                return error(e, HttpStatus.CONFLICT);
            }
        }
        return error("No data Input", HttpStatus.NO_CONTENT);

    }

    @RequestMapping(value = "/enabled-dashboard-preferences/{userId}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> loadEndabledDashlets(@PathVariable("userId") Long userId) {
        List<UserDashboardReference> userDashboardReferenceList = null;
        userDashboardReferenceList = service.loadEndabledDashlets(userId);
        return OpenLmisResponse.response(PREFERENCES, userDashboardReferenceList);

    }

    @RequestMapping(value = "/disabled-dashboard-preferences/{userId}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> loadDisabledDashlets(@PathVariable("userId") Long userId) {
        List<UserDashboardReference> userDashboardReferenceList = null;
        userDashboardReferenceList = service.loadDisabledDashlets(userId);
        return OpenLmisResponse.response(PREFERENCES, userDashboardReferenceList);
    }

    @RequestMapping(value = "/dashboard-preference/{userId}/{dashboard}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> loaduserPreference(@PathVariable("userId") Long userId, @PathVariable("dashboard") String dashboard) {
        UserDashboardReference preference = service.loaduserPreference(userId, dashboard);
        return OpenLmisResponse.response(PREFERENCE, preference);
    }
}
