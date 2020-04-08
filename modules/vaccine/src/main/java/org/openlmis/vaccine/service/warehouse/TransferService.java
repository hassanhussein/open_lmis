package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.repository.warehouse.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferService {

    @Autowired
    private TransferRepository repository;

    @Transactional
    public Transfer save(Transfer item, Long loggedInUserId) {

       if(item.getId() == null) {
           repository.insert(item);
       }else
           repository.update(item);

       return item;

    }


    public List<Transfer> search(String searchParam) {
        return null;
    }

    public List<Transfer> getAll() {
        return null;
    }
}
