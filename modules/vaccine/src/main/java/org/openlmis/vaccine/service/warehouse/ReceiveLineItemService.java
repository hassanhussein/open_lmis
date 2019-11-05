package org.openlmis.vaccine.service.warehouse;
import org.openlmis.vaccine.domain.wms.ReceiveLineItem;
import org.openlmis.vaccine.repository.warehouse.ReceiveLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiveLineItemService {

    @Autowired
    private ReceiveLineItemRepository repository;

    public void save(ReceiveLineItem receiveLineItem) {

        if (receiveLineItem.getId() == null) {

            repository.insert(receiveLineItem);
        }else {
            repository.update(receiveLineItem);
        }

    }

    public ReceiveLineItem getById (Long id) {
        return repository.getById(id);
    }

    public List<ReceiveLineItem> getAll(){
        return  repository.getAll();
    }


    public List<ReceiveLineItem> getByReceiveId(Long id) {

        return repository.getByReceiveId(id);
    }

    public void deleteByReceiveId(Long id) {
        repository.deleteByReceiveId(id);
    }
}
