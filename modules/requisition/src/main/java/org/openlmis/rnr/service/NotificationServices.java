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


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.ConfigurationSettingKey;
import org.openlmis.core.domain.Subscribers;
import org.openlmis.core.domain.User;
import org.openlmis.core.service.*;
import org.openlmis.email.service.EmailService;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.dto.FacilityLevelRequisitionStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class NotificationServices {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServices.class);
  private static final String GOTHOMIS = "GOTHOMIS";
  private static final String GOTHOMIS_USERNAME = "GOTHOMIS_USERNAME";
  private static final String GOTHOMIS_PASSWORD = "GOTHOMIS_PASSWORD";
  private static final String GOTHOMIS_URL = "GOTHOMIS_URL";
  private static final String GRANT_TYPE = "GRANT_TYPE";
  private static final String URL_POST_GOTHMIS = "URL_POST_GOTHMIS";
  private static final String TOKEN_ACCESS_USERNAME = "TOKEN_ACCESS_USERNAME";
  private static final String TOKEN_ACCESS_PASSWORD = "TOKEN_ACCESS_PASSWORD";

  @Value("${mail.base.url}")
  String baseURL;

  @Autowired
  private ConfigurationSettingService configService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private ApproverService approverService;

  @Autowired
  private RequisitionEmailServiceForSIMAM requisitionEmailServiceForSIMAM;

  @Autowired
  private StaticReferenceDataService staticReferenceDataService;

  @Autowired
  private UserService userService;


  RestTemplate restTemplate;

  public void notifyRequisitionToFacilityLvelSystems(Rnr requisition){

    String username = configService.getByKey(GOTHOMIS_USERNAME).getValue();
    String password = configService.getByKey(GOTHOMIS_PASSWORD).getValue();
    String url = configService.getByKey(GOTHOMIS_URL).getValue();
    String gotUrl = configService.getByKey(URL_POST_GOTHMIS).getValue();
    String grantType = configService.getByKey(GRANT_TYPE).getValue();
    String tokenAccessUsername = configService.getByKey(TOKEN_ACCESS_USERNAME).getValue();
    String tokenAccessPassword = configService.getByKey(TOKEN_ACCESS_PASSWORD).getValue();


    String authStr = username+":"+password;
    String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Basic " + base64Creds);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
    map.add("grant_type", grantType);
    map.add("username", tokenAccessUsername);
    map.add("password", tokenAccessPassword);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

    ResponseEntity<String> response = new RestTemplate().postForEntity( url, request , String.class );

    System.out.println(response.getBody());
     sendToFacility(gotUrl, response.getBody(), requisition);

  }

  private void sendToFacility(String url, String tocken, Rnr rnr) {
    ObjectMapper mapper = new ObjectMapper();

    User user =  userService.getById(rnr.getModifiedBy());

    try {

      FacilityLevelRequisitionStatusDTO statusData = mapper.readValue(tocken, FacilityLevelRequisitionStatusDTO.class);
      System.out.println(statusData.getAccessToken());
      String baseUrl = url + "?access_token="+statusData.getAccessToken();
      System.out.println(baseUrl);
      HttpHeaders headers = new HttpHeaders();
      // headers.add("Authorization", "Basic " + base64Creds);
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
      map.add("access_token", statusData.getAccessToken());
      map.add("status", rnr.getStatus().toString());
      map.add("rnrId", rnr.getId().toString());
      map.add("approverName", user.getFirstName() +" "+user.getLastName());
      //map.add("username", "matoto@gmail.com");
      // map.add("password", "12345678");

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

      ResponseEntity<String> response = new RestTemplate().postForEntity( baseUrl, request , String.class );

      System.out.println(response.getBody());


    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void notifyStatusChange(Rnr requisition) {


    List <Subscribers> subscribers = null;
    final String telegram_send_notification_base_url = "https://api.telegram.org/bot" +  staticReferenceDataService.getPropertyValue("bot.token") +"/sendMessage?chat_id=";
    String support_content = null;

    subscribers = approverService.getListOfSubscribers(requisition.getId());

    if(subscribers != null){
        for(Subscribers subscriber : subscribers)
        {
            try
            {
              support_content = "RnR for facility " + requisition.getFacility().getName() + " " + requisition.getFacility().getFacilityType().getName() + " for period " + requisition.getPeriod().getName() + " " + requisition.getPeriod().getStringYear()  + " has been " + requisition.getStatus().name();

              URL url = new URL(telegram_send_notification_base_url + subscriber.getChatId() + "&text=" + URLEncoder.encode(support_content, "UTF-8"));
              HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
              BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
              rd.close();
            } catch (Exception e){
                e.fillInStackTrace();
            }
        }

     }


    List<User> users = null;
    // find out which email to send it to
    switch (requisition.getStatus()) {
      // this order has been submitted
      case SUBMITTED:
        // all that can fill for the facility
        users = approverService.getFacilityBasedAutorizers(requisition.getId());
        break;
      case AUTHORIZED:
        users = approverService.getNextApprovers(requisition.getId());
        break;
      case IN_APPROVAL:
        users = approverService.getNextApprovers(requisition.getId());
        break;
      case RELEASED:
      default:
        break;
    }

    if (users != null) {

      if (staticReferenceDataService.getBoolean("toggle.email.attachment.simam")) {
        //catch all the issues when creating file
        try {
          requisitionEmailServiceForSIMAM.sendRequisitionEmailWithAttachment(requisition, users);
        } catch (Throwable t) {
          LOGGER.error("There is a error when creating requisition email: " + t.getMessage());
        }
        return;
      }

      for (User user : users) {
        if (user.isMobileUser() || user.getActive() != Boolean.TRUE || user.getVerified() != Boolean.TRUE || !user.getReceiveSupervisoryNotifications()) {
          continue;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        String emailMessage = configService.getByKey(ConfigurationSettingKey.EMAIL_TEMPLATE_APPROVAL).getValue();

        String approvalURL = String.format("%1$s/public/pages/logistics/rnr/index.html#/rnr-for-approval/%2$s/%3$s?supplyType=full-supply&page=1", baseURL, requisition.getId(), requisition.getProgram().getId());

        emailMessage = emailMessage.replaceAll("\\{facility_name\\}", requisition.getFacility().getName());
        emailMessage = emailMessage.replaceAll("\\{program_name\\}", requisition.getProgram().getName());
        emailMessage = emailMessage.replaceAll("\\{approver_name\\}", user.getFirstName() + " " + user.getLastName());
        emailMessage = emailMessage.replaceAll("\\{period\\}", requisition.getPeriod().getName());
        emailMessage = emailMessage.replaceAll("\\{link\\}", approvalURL);

        message.setText(emailMessage);
        message.setSubject(configService.getByKey(ConfigurationSettingKey.EMAIL_SUBJECT_APPROVAL).getValue());
        message.setTo(user.getEmail());

        try {
          emailService.queueMessage(message);
        } catch (Exception exp) {
          LOGGER.error("Notification was not sent due to the following exception ...", exp);
        }
      }
    }
  }


  public void sendRejectedEmail(Rnr requisition, String reasons) {

    List<User> users = userService.getUsersByHomeFacility(requisition.getFacility().getId());

    if(!users.isEmpty()) {

      for (User user : users) {

        SimpleMailMessage message = new SimpleMailMessage();
        String emailMessage = configService.getByKey(ConfigurationSettingKey.EMAIL_TEMPLATE_REJECTION).getValue();

        String approvalURL = String.format("%1$s/public/pages/logistics/rnr/index.html#/init-rnr", baseURL, requisition.getId(), requisition.getProgram().getId());

        emailMessage = emailMessage.replaceAll("\\{facility_name\\}", requisition.getFacility().getName());
        emailMessage = emailMessage.replaceAll("\\{program_name\\}", requisition.getProgram().getName());
        emailMessage = emailMessage.replaceAll("\\{receiver_name\\}", user.getFirstName() + " " + user.getLastName());
        emailMessage = emailMessage.replaceAll("\\{period\\}", requisition.getPeriod().getName());
        emailMessage = emailMessage.replaceAll("\\{reasons\\}", reasons);
        emailMessage = emailMessage.replaceAll("\\{link\\}", approvalURL);

        message.setText(emailMessage);
        message.setSubject(configService.getByKey(ConfigurationSettingKey.EMAIL_SUBJECT_APPROVAL).getValue());
        message.setTo(user.getEmail());

        try {
          emailService.queueMessage(message);
        } catch (Exception exp) {
          LOGGER.error("Notification was not sent due to the following exception ...", exp);
        }
      }


    }

  }
}
