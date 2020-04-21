package org.openlmis.core.service.notification;

import org.openlmis.core.dto.notification.CloseToExpireItem;
import org.openlmis.core.dto.notification.StockOutItem;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.notification.CloseToExpireRepository;
import org.openlmis.core.repository.notification.StockOutItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockOutItemService {

    @Autowired
    private StockOutItemRepository repository;

    public void save(StockOutNotificationDTO notification) {

        if(!notification.getStockOutItems().isEmpty()) {
            repository.deleteByNotificationId(notification.getId());

            for(StockOutItem item : notification.getStockOutItems()) {
                item.setModifiedBy(notification.getModifiedBy());
                item.setCreatedBy(notification.getCreatedBy());
                item.setNotificationId(notification.getId());
                repository.insert(item);
            }

        }

    }

}
