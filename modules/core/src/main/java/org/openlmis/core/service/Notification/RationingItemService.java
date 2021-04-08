package org.openlmis.core.service.notification;

import org.openlmis.core.dto.notification.RationingItem;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.notification.RationingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RationingItemService {

    @Autowired
    private RationingItemRepository repository;

    public void save(StockOutNotificationDTO notification) {

        if(!notification.getInSufficientFundingItems().isEmpty()) {
            repository.deleteByNotificationId(notification.getId());

            for(RationingItem item : notification.getRationingItems()) {
                item.setModifiedBy(notification.getModifiedBy());
                item.setCreatedBy(notification.getCreatedBy());
                item.setNotificationId(notification.getId());
                repository.insert(item);
            }

        }

    }

}
