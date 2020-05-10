package org.openlmis.restapi.controller;

import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.exception.DataException;


import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.restapi.service.RestStockNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
@NoArgsConstructor
public class StockOutNotificationController extends BaseController {

@Autowired
private RestStockNotificationService service;

    @ApiOperation(value = "Accepts Out of Stock Notification Status!", httpMethod = "POST")
    @RequestMapping(value = "/rest-api/oos-notification.json", method = RequestMethod.POST)
    public ResponseEntity<OpenLmisResponse> submitOoos(@RequestBody StockOutNotificationDTO notification, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return OpenLmisResponse.error(bindingResult.getGlobalError().toString(), HttpStatus.BAD_REQUEST);
        }
        notification.setCreatedBy(loggedInUserId(request.getUserPrincipal()));
        notification.setModifiedBy(loggedInUserId(request.getUserPrincipal()));
        try {
            service.save(notification,request);
        } catch (SQLException exception) {
            throw new DataException(exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  service.sendResponse(notification);
        return OpenLmisResponse.success("Saved Successiful!");
    }
}
