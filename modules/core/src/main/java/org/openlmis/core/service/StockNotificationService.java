package org.openlmis.core.service;

import org.openlmis.core.domain.RightName;
import org.openlmis.core.dto.NotificationResponseDTO;
import org.openlmis.core.dto.ResponseExtDTO;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.StockNotificationRepository;
import org.openlmis.core.repository.helper.CommaSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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


    @Transactional
 public StockOutNotificationDTO save(StockOutNotificationDTO notification) throws SQLException {

     if (notification != null) {

         StockOutNotificationDTO not = repository.getByInvoiceNumber(notification.getInvoiceNumber());

         if (not == null) {
             repository.insert(notification);
         } else {
             notification.setId(not.getId());
             repository.update(notification);
         }
     }

      return notification;
 }


    public void sendResponse(StockOutNotificationDTO notification) {
        NotificationResponseDTO notificationResponse = new NotificationResponseDTO();
        notificationResponse.setInvoiceNumber(notification.getInvoiceNumber());
        notificationResponse.setMessage("Received Successful");
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
