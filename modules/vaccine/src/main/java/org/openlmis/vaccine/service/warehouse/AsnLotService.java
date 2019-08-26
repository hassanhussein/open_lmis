package org.openlmis.vaccine.service.warehouse;
import org.openlmis.vaccine.domain.wms.AsnLot;
import org.openlmis.vaccine.repository.warehouse.AsnLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsnLotService {

     @Autowired
    AsnLotRepository repository;

    public void save(AsnLot asnLot) {

        if (asnLot.getId() == null) {

            repository.insert(asnLot);
        }else {
            repository.update(asnLot);
        }

    }

    public AsnLot getById (Long id) {

        return repository.getById(id);

    }
    public List<AsnLot> getAll(){

        return  repository.getAll();
    }
}
