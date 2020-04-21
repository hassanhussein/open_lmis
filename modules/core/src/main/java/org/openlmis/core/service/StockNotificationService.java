package org.openlmis.core.service;

import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.StockNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class StockNotificationService {

 @Autowired
 private StockNotificationRepository repository;

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



}
