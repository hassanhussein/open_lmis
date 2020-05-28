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

package org.openlmis.rnr.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.openlmis.core.domain.ConfigurationSettingKey;
import org.openlmis.core.domain.OnScreenNotification;
import org.openlmis.core.domain.User;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.service.OnScreenNotificationService;
import org.openlmis.email.service.EmailService;
import org.openlmis.rnr.domain.RequisitionStatusChange;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.domain.Template;
import org.openlmis.rnr.repository.RequisitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequisitionNotificationService {

  @Autowired
  OnScreenNotificationService onScreenNotificationService;

  @Autowired
  ConfigurationSettingService configurationSettingService;

  @Autowired
  RequisitionRepository requisitionRepository;

  @Autowired
  EmailService emailService;


  public void notifyRequisitionRejected(Rnr rnr) {
    List<RequisitionStatusChange> changes = requisitionRepository.getAllStatusChanges(rnr.getId());
    List<User> users = changes.stream().map(c->c.getCreatedBy()).distinct().collect(Collectors.toList());
    // log this message in a table so that the notification can be seen on screen.
    notifyOnScreen(rnr, users);
    notifyByEmail(rnr, users);
  }

  private void notifyByEmail(Rnr rnr, List<User> users){
    // find the template and generate the email message.
    String bodyTemplate = configurationSettingService.getConfigurationStringValue(ConfigurationSettingKey.EMAIL_TEMPLATE_FOR_REJECTED_REQUISITION);
    String subjectTemplate = configurationSettingService.getConfigurationStringValue(ConfigurationSettingKey.EMAIL_SUBJECT_FOR_REJECTED_REQUISITION);

    for(User user: users) {
      Map<String, String> model = new HashMap<>();
      model.put("facility", rnr.getFacility().getName());
      model.put("period", rnr.getPeriod().getName());
      model.put("program", rnr.getProgram().getName());
      model.put("url", generateUrl(rnr));
      model.put("name", user.getFullName());
      emailService.queueHtmlMessage(user.getEmail(),subjectTemplate, bodyTemplate, model);
    }
  }

  private void notifyOnScreen(Rnr rnr, List<User> users) {
    List<OnScreenNotification> notifications = new ArrayList<>();
    users.forEach(u->{
      OnScreenNotification un = OnScreenNotification
          .builder()
          .facilityId(rnr.getFacility().getId())
          .requisitionId(rnr.getId())
          .fromUserId(rnr.getModifiedBy())
          .toUserId(u.getId())
          .isHandled(false)
          .type("REJECTED_REQUISITION")
          .message("Réquisition rejetée:" +
              "Cliquez ici pour <a href=\"" + generateUrl(rnr) + "\">modifier et soumettre à nouveau</a>.")
          .subject("Réquisition rejetée: " + rnr.getFacility().getName() + " " + rnr.getPeriod().getName())
          .url(generateUrl(rnr))
          .build();
      notifications.add(un);
    });

    if(notifications.size() > 0){
      onScreenNotificationService.sendNotification(notifications);
    }
  }

  private String generateUrl(Rnr rnr) {
    return "/public/pages/logistics/rnr/index.html#/create-rnr/" + rnr.getId()+ "/" + rnr.getFacility().getId()+ "/" + rnr.getProgram().getId();
  }

}
