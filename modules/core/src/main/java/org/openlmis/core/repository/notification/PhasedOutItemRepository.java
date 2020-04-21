package org.openlmis.core.repository.notification;

import org.openlmis.core.dto.notification.PhasedOutItem;
import org.openlmis.core.repository.mapper.notificationMapper.PhasedOutItemMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

@Component
public class PhasedOutItemRepository {

    @Autowired
    private PhasedOutItemMapper mapper;


    public Integer insert(PhasedOutItem item) {
        return mapper.insert(item);
    }

    public void update(PhasedOutItem item) {
        mapper.update(item);
    }

    public void deleteByNotificationId(Long id) {
        mapper.deleteByNotificationId(id);
    }
}
