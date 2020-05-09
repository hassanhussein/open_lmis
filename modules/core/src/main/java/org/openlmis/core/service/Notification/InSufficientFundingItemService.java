package org.openlmis.core.service.Notification;

import org.openlmis.core.dto.notification.InSufficientFundingItem;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.notification.InSufficientFundingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InSufficientFundingItemService {

    @Autowired
    private InSufficientFundingItemRepository repository;

    public void save(StockOutNotificationDTO notification) {

        if(!notification.getInSufficientFundingItems().isEmpty()) {
            repository.deleteByNotificationId(notification.getId());

            for(InSufficientFundingItem item : notification.getInSufficientFundingItems()) {
                item.setModifiedBy(notification.getModifiedBy());
                item.setCreatedBy(notification.getCreatedBy());
                item.setNotificationId(notification.getId());
                repository.insert(item);
            }

        }

    }


}
