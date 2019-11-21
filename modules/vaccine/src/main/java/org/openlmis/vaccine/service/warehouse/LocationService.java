package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.Location;
import org.openlmis.vaccine.repository.warehouse.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;

    public void save(Location location){

        List<Location> locationList = repository.getAllBy(location.getWareHouseId());

        if(locationList == null) {
            repository.insert(location);
        }else {
            repository.deleteBy(location.getWareHouseId());
            repository.insert(location);
        }

    }

}
