package org.openlmis.core.repository.notification;

import org.openlmis.core.dto.notification.CloseToExpireItem;
import org.openlmis.core.repository.mapper.notificationMapper.CloseToExpireItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CloseToExpireRepository {

    @Autowired
    private CloseToExpireItemMapper mapper;

    public Integer insert(CloseToExpireItem item) {
        return mapper.insert(item);
    }

    public void update(CloseToExpireItem item) {
        mapper.update(item);
    }

    public void deleteByNotificationId(Long id) {
        mapper.deleteByNotificationId(id);
    }
}
