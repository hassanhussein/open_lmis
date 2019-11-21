package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.Zone;
import org.openlmis.vaccine.repository.mapper.warehouse.location.ZoneMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZoneRepository {

    private ZoneMapper mapper;

    public List<Zone> getAll(){

        return mapper.getAll();
    }

}
