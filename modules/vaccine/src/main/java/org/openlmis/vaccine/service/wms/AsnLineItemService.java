package org.openlmis.vaccine.service.wms;

import org.openlmis.vaccine.domain.wms.AsnLineItem;
import org.openlmis.vaccine.repository.wms.AsnLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsnLineItemService {

    @Autowired
    AsnLineItemRepository repository;

    public void save(AsnLineItem asnLineItem) {

        if (asnLineItem.getId() == null) {

            repository.insert(asnLineItem);
        }else {
            repository.update(asnLineItem);
        }

    }

    public AsnLineItem getById (Long id) {

        return repository.getById(id);

    }
    public List<AsnLineItem> getAll(){

        return  repository.getAll();
    }

}
