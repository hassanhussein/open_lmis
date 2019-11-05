package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.ReceiveLot;
import org.openlmis.vaccine.repository.warehouse.ReceiveLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiveLotService {

    @Autowired
    ReceiveLotRepository repository;

    public void save(ReceiveLot lot) {
    //   ReceiveLot l = repository.getByLotNumber(lot.getLotNumber());

        if (lot.getId() == null) {

            repository.insert(lot);
        }else {
            repository.update(lot);
        }

    }

    public ReceiveLot getById (Long id) {

        return repository.getById(id);

    }
    public List<ReceiveLot> getAll(){

        return  repository.getAll();
    }

    public List<ReceiveLot> getByLineItem(Long id) {
        return repository.getByLotNumber(id);
    }

    public void deleteByLineItem(Long id) {
        repository.deleteByLineItem(id);
    }
}
