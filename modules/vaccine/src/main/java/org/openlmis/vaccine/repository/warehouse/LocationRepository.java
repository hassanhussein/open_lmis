package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.Location;
import org.openlmis.vaccine.repository.mapper.warehouse.location.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationRepository {
    @Autowired
    private LocationMapper mapper;

    public Integer insert(Location location) {
       return mapper.insert(location);
    }

    public void update(Location location) {
        mapper.update(location);
    }

    public Location getByCode(String code) {
        return mapper.getByCode(code);
    }

    public void deleteBy(Long wareHouseId) {
        mapper.deleteBy(wareHouseId);
    }

    public List<Location> getAllBy(Long wareHouseId) {
        return mapper.getByWareHouse(wareHouseId);
    }


}
