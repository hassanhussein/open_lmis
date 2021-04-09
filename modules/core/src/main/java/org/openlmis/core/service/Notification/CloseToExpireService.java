package org.openlmis.core.service.notification;

import org.openlmis.core.dto.notification.CloseToExpireItem;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.notification.CloseToExpireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CloseToExpireService {

    @Autowired
    private CloseToExpireRepository repository;


    public void save(StockOutNotificationDTO notification) {

        if(!notification.getCloseToExpireItems().isEmpty()) {

            repository.deleteByNotificationId(notification.getId());

            for(CloseToExpireItem item : notification.getCloseToExpireItems()) {
                item.setModifiedBy(notification.getModifiedBy());
                item.setCreatedBy(notification.getCreatedBy());
                item.setNotificationId(notification.getId());
                repository.insert(item);
            }

        }

    }


}
