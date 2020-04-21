package org.openlmis.core.repository.notification;

import org.openlmis.core.dto.notification.StockOutItem;
import org.openlmis.core.repository.mapper.notificationMapper.StockOutItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockOutItemRepository {

    @Autowired
    private StockOutItemMapper mapper;

    public Integer insert(StockOutItem item) {
        return mapper.insert(item);
    }

    public void update(StockOutItem item) {
        mapper.update(item);
    }

    public void deleteByNotificationId(Long id) {
        mapper.deleteByNotificationId(id);
    }
}
