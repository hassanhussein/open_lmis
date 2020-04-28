package org.openlmis.core.repository.notification;

import org.openlmis.core.dto.notification.RationingItem;
import org.openlmis.core.repository.mapper.notificationMapper.RationingItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RationingItemRepository {

    @Autowired
    private RationingItemMapper mapper;


    public Integer insert(RationingItem item) {
        return mapper.insert(item);
    }

    public void update(RationingItem item) {
        mapper.update(item);
    }

    public void deleteByNotificationId(Long id) {
          mapper.deleteByNotificationId(id);
    }
}
