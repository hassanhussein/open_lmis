package org.openlmis.vaccine.service.warehouse;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.vaccine.domain.wms.Zone;
import org.openlmis.vaccine.repository.warehouse.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository repository;

    public List<Zone> getAll(){
        return repository.getAll();
    }

    public Zone getByCode(Zone zone){
        return repository.getByCode(zone.getCode());
    }

    public void save(Zone zone) {

        if(zone.getId() == null) {
            repository.insert(zone);
        }else {
            repository.update(zone);
        }
    }
}
