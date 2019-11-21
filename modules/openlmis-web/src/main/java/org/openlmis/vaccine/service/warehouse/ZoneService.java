package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.Zone;
import org.openlmis.vaccine.repository.warehouse.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private ZoneRepository repository;

    public List<Zone> getAll(){
        return repository.getAll();
    }
}
