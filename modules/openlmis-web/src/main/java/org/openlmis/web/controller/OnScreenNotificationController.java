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

package org.openlmis.web.controller;

import org.openlmis.core.service.OnScreenNotificationService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class OnScreenNotificationController extends BaseController {

  @Autowired
  OnScreenNotificationService service;

  @RequestMapping(value = "/notifications/on-screen-notifications", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getOnScreenNotification(HttpServletRequest request) {
    return OpenLmisResponse.response("notifications", service.getNotifications(loggedInUserId(request)));
  }

  @RequestMapping(value = "/notifications/on-screen-notifications-count", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getOnScreenNotificationCount(HttpServletRequest request) {
    return OpenLmisResponse.response("notifications", service.getNotificationsCount(loggedInUserId(request)));
  }

  @RequestMapping(value = "/notifications/mark-as-read", method = RequestMethod.PUT)
  public ResponseEntity<OpenLmisResponse> markAsRead(@RequestParam("rnrId") Long rnrId) {
    service.markAsRead(service.getNotificationByRequisitionId(rnrId));
    return OpenLmisResponse.success("marked as read successfully.");
  }


}
