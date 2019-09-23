package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.ReceiveLineItem;
import org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveLineItemRepository {
    @Autowired
    private ReceiveLineItemMapper mapper;

    public Integer insert(ReceiveLineItem lineItem ){
        return mapper.insert(lineItem);
    }

    public void update(ReceiveLineItem lineItem) {
        mapper.update(lineItem);
    }


}
