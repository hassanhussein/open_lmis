package org.openlmis.core.service.notification;

import org.openlmis.core.dto.notification.FullFilledItem;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.notification.FullFilledItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FullFilledItemService {

    @Autowired
    private FullFilledItemRepository repository;

    public void save(StockOutNotificationDTO notification) {

        if(!notification.getFullFilledItems().isEmpty()) {
            repository.deleteByNotificationId(notification.getId());

            for(FullFilledItem item : notification.getFullFilledItems()) {
                item.setModifiedBy(notification.getModifiedBy());
                item.setCreatedBy(notification.getCreatedBy());
                item.setNotificationId(notification.getId());
                repository.insert(item);
            }

        }

    }

}
