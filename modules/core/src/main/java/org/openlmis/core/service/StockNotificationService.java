package org.openlmis.core.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.RightName;
import org.openlmis.core.dto.NotificationResponseDTO;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.StockNotificationRepository;
import org.openlmis.core.repository.helper.CommaSeparator;
import org.openlmis.core.service.notification.view.NotificationPdfView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockNotificationService {

 @Autowired
 private StockNotificationRepository repository;

 @Autowired
 private ELMISInterfaceService elmisInterfaceService;

 @Autowired
 private CommaSeparator commaSeparator;

 @Autowired
 private FacilityService facilityService;

 @Autowired
 private MessageService messageService;


 @Transactional
 public StockOutNotificationDTO save(StockOutNotificationDTO notification, HttpServletRequest request) throws Exception {

     if (notification != null) {

         StockOutNotificationDTO not = repository.getByInvoiceNumber(notification.getInvoiceNumber());

         if (not == null) {
             repository.insert(notification);
         } else {
             notification.setId(not.getId());
             repository.update(notification);
         }

         prepareEmailNotification(notification,request);

     }

      return notification;
 }

    private void prepareEmailNotification(StockOutNotificationDTO notificationData, HttpServletRequest request) throws Exception {

        StockOutNotificationDTO notification = getById(notificationData.getId());
        Facility facility  = facilityService.getByCodeFor(notification.getSoldTo());

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("notification", notification);
        map.put("facility",facility);
        NotificationPdfView view = new NotificationPdfView(messageService);

        view.render(map, request, null);

    }


    public void sendResponse(StockOutNotificationDTO notification) {
        NotificationResponseDTO notificationResponse = new NotificationResponseDTO();
        notificationResponse.setInvoiceNumber(notification.getInvoiceNumber());
        notificationResponse.setMessage("Received Successful");
        notificationResponse.setSource("eLMIS");
        elmisInterfaceService.processAndSendOutOfStockResponseData(notificationResponse);
    }

    public StockOutNotificationDTO getById(Long id) {
     return repository.getById(id);
    }

    public List<HashMap<String,Object>> getStockOutBy(Long userId, Long programId){
        String facilityIds = commaSeparator.commaSeparateIds(facilityService.getUserSupervisedFacilities(userId, programId, RightName.VIEW_OUT_OF_STOCK_NOTIFICATION));
        return repository.getStockBy(facilityIds);
    }

}
