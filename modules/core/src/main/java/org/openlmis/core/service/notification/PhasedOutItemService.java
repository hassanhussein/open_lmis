package org.openlmis.core.service.notification;

import org.openlmis.core.dto.notification.InSufficientFundingItem;
import org.openlmis.core.dto.notification.PhasedOutItem;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.notification.PhasedOutItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhasedOutItemService {

    @Autowired
    private PhasedOutItemRepository repository;

    public void save(StockOutNotificationDTO notification) {

        if(!notification.getInSufficientFundingItems().isEmpty()) {
            repository.deleteByNotificationId(notification.getId());

            for(PhasedOutItem item : notification.getPhasedOutItems()) {
                item.setModifiedBy(notification.getModifiedBy());
                item.setCreatedBy(notification.getCreatedBy());
                item.setNotificationId(notification.getId());
                repository.insert(item);
            }

        }

    }
}
