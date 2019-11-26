package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.WareHouseLineItem;
import org.openlmis.vaccine.repository.mapper.warehouse.location.WareHouseLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WareHouseLineItemRepository {

    @Autowired
    private WareHouseLineItemMapper mapper;

    public void deleteByWareHouseId(Long id) {
        mapper.deleteByWareHouseId(id);
    }

    public Integer insert(WareHouseLineItem lineItem) {
        return mapper.insert(lineItem);
    }
}
