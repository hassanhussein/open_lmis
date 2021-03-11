
/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.web.controller;


import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.notification.domain.Notifications;
import org.openlmis.notification.service.FacilityNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

import static org.openlmis.core.web.OpenLmisResponse.success;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value = "/notification/")
public class FacilityNotificationController extends BaseController {
    @Autowired
    private FacilityNotificationService service;
    private final static String NOTIFICATION = "notification";
    private final static String NOTIFICATIONS = "notifications";

    @RequestMapping(value = "notifications", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> save(@RequestBody Notifications notification, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            notification.setModifiedBy(userId);
            notification.setCreatedBy(userId);
            notification.setCreatedDate(new Date());
            notification.setModifiedDate(new Date());
            service.addNewNotification(notification);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Notifications Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Notifications '%s' has been successfully saved", notification.getName()));
        successResponse.getBody().addData(NOTIFICATION, notification);
        return successResponse;
    }

    @RequestMapping(value = "notifications", method = PUT, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> update(@RequestBody Notifications notification, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            notification.setModifiedBy(userId);
            notification.setCreatedBy(userId);
            service.updateNotifciation(notification);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Notifications Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Notifications '%s' has been successfully updated", notification.getName()));
        successResponse.getBody().addData(NOTIFICATION, notification);
        return successResponse;
    }

    @RequestMapping(method = GET, value = "notifications")
    public ResponseEntity<OpenLmisResponse> getNotifications() {
        return OpenLmisResponse.response(NOTIFICATIONS, service.getAllNotifications());
    }

    @RequestMapping(method = GET, value = "notifications/{id}")
    public ResponseEntity<OpenLmisResponse> getNotificationById(@PathVariable("id") Long notificationId) {
        return OpenLmisResponse.response(NOTIFICATION, service.getNotificationById(notificationId));
    }

    @RequestMapping(method = GET, value = "new-notifications/{facilityCode}")
    public ResponseEntity<OpenLmisResponse> getNewNotificationByFacilityCode(@PathVariable("facilityCode") String facilityCode) {
        return OpenLmisResponse.response(NOTIFICATIONS, service.getNewNotificationByFacilityCode(facilityCode));
    }


    @RequestMapping(method = GET, value = "new-notifications/{facilityCode}/{acknowledged}")
    public ResponseEntity<OpenLmisResponse> getNotificationByFacilityCode(@PathVariable("facilityCode") String facilityCode, @PathVariable("acknowledged") Boolean acknowledged) {
        return OpenLmisResponse.response(NOTIFICATIONS, service.getNotificationByFacilityCode(facilityCode, acknowledged));
    }


}
