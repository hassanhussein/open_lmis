package org.openlmis.vaccine.repository.warehouse;


import org.openlmis.vaccine.domain.wms.AsnLineItem;
import org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AsnLineItemRepository {

    @Autowired
    AsnLineItemMapper mapper;

    public Integer insert(AsnLineItem asnLineItem) {
        return mapper.insert(asnLineItem);
    }

    public void update(AsnLineItem asnLineItem) {
        mapper.update(asnLineItem);
    }

    public AsnLineItem getById (Long id){
        return mapper.getById(id);
    }

    public List<AsnLineItem> getAll(){
        return mapper.getAll();
    }
}
