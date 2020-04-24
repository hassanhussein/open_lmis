package org.openlmis.core.repository.notification;
import org.openlmis.core.dto.notification.FullFilledItem;
import org.openlmis.core.repository.mapper.notificationMapper.FullFilledItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FullFilledItemRepository {

    @Autowired
    private FullFilledItemMapper mapper;

    public Integer insert(FullFilledItem item) {
        return mapper.insert(item);
    }

    public void update(FullFilledItem item) {
        mapper.update(item);
    }

    public void deleteByNotificationId(Long id) {
        mapper.deleteByNotificationId(id);
    }
}
