/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 *
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.restapi.controller;

import org.openlmis.core.exception.DataException;
import org.openlmis.notification.domain.Notifications;
import org.openlmis.notification.service.ProducerService;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.restapi.service.RestFacilityNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RestFacilityNotificationController extends BaseController {
    public final static String NOTIFICATIONS = "notifications";
    @Autowired
    RestFacilityNotificationService service;
    @Autowired
    ProducerService messageProducerService;

    @RequestMapping(value = "/rest-api/facility-notifications/{facilityCode}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getFacilityNotificationByCode(@PathVariable String facilityCode, Principal principal) {
        List<Notifications> facilityNotificationList;
        try {
            Long userId=this.loggedInUserId(principal);
            facilityNotificationList = service.loadFacilityNotification(facilityCode,userId);
        } catch (DataException e) {
            return RestResponse.error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
        return RestResponse.response(NOTIFICATIONS, facilityNotificationList);
    }
    @RequestMapping(value = "/rest-api/facility-notifications/test", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> testMessage(@RequestBody String message, Principal principal) {
        List<Notifications> facilityNotificationList;
        try {
            Long userId=this.loggedInUserId(principal);
           messageProducerService.produceMessage(message);
        } catch (DataException e) {
            return RestResponse.error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
        return RestResponse.response("test Message ", "delivered Properly and the message delivered is "+ message);
    }

}
