package org.openlmis.web.controller;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.service.FacilityService;

import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.restapi.service.RestStockNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.openlmis.core.web.OpenLmisResponse.response;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@NoArgsConstructor
public class StockNotificationController extends BaseController {

   /* @Autowired
    private NotificationServices services;
*/

    @Autowired
    private RestStockNotificationService stockNotificationService;

    @Autowired
    private FacilityService facilityService;

    @RequestMapping(value = "/stock/{id}", method = GET, headers = ACCEPT_PDF)
    public ModelAndView printNotification(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("notificationPDF");

        StockOutNotificationDTO notification = stockNotificationService.getById(id);
        Facility facility  = facilityService.getByCodeFor(notification.getSoldTo());

        modelAndView.addObject("notification",notification);
        modelAndView.addObject("facility",facility);
        System.out.println(notification.getInvoiceNumber());
        return modelAndView;
    }

    @RequestMapping(value = "/all-notifications/{id}", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public StockOutNotificationDTO getAllNots(@PathVariable Long id) {
        return stockNotificationService.getById(id);
    }

    @RequestMapping(value = "/stock-notification/{program}", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_OUT_OF_STOCK_NOTIFICATION')")
    public ResponseEntity<OpenLmisResponse> getStockOutBy(@PathVariable Long program, HttpServletRequest request) {
        return response("notifications", stockNotificationService.getStockOutBy(loggedInUserId(request),program));
    }

   @RequestMapping(value = "/stock-notification-details/{id}", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'VIEW_OUT_OF_STOCK_NOTIFICATION')")
    public ResponseEntity<OpenLmisResponse> getList(@PathVariable Long id, HttpServletRequest request) {
       List<HashMap<String,Object>> list = new ArrayList<>();

       StockOutNotificationDTO notification = stockNotificationService.getById(id);

        return response("notifications", stockNotificationService.getStockOutBy(loggedInUserId(request),id));
    }

}
