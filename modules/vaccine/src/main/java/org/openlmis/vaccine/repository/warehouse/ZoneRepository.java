package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.Zone;
import org.openlmis.vaccine.repository.mapper.warehouse.location.ZoneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZoneRepository {

    @Autowired
    private ZoneMapper mapper;

    public List<Zone> getAll(){

        return mapper.getAll();
    }

    public Zone getByCode(String code) {
        return mapper.getByCode(code);
    }

    public Integer insert(Zone zone) {
       return mapper.insert(zone);
    }

    public void update(Zone zone) {
        mapper.update(zone);
    }
}
