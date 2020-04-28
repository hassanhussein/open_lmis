package org.openlmis.core.repository.notification;

import org.openlmis.core.dto.notification.InSufficientFundingItem;
import org.openlmis.core.repository.mapper.notificationMapper.InSufficientFundingItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InSufficientFundingItemRepository {

    @Autowired
    private InSufficientFundingItemMapper mapper;

    public Integer insert(InSufficientFundingItem item) {
        return mapper.insert(item);
    }

    public void update(InSufficientFundingItem item) {
        mapper.update(item);
    }

    public void deleteByNotificationId(Long id) {
        mapper.deleteByNotificationId(id);
    }
}
