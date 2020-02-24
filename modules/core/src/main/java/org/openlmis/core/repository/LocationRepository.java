package org.openlmis.core.repository;

import org.openlmis.core.domain.Location;
import org.openlmis.core.domain.LocationType;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.repository.mapper.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class LocationRepository {

    @Autowired
    private LocationMapper mapper;

    public void save(Location location){

        if (location.getId() == null){
            mapper.insert(location);
        }else {
            mapper.update(location);
        }
    }

    public List<Location> getAllLocations(){
        return mapper.getAllLocations();
    }

    public Location getById(Long id){
        return mapper.getById(id);
    }

    public void deleteById(Long id){

          mapper.deleteById(id);
    }
    public  Location getByCode(String code){

        return  mapper.getByCode(code);
    }

    public List<LocationType> getAllLocationTypes(){
        return  mapper.getAllLocationTypes();
    }
    public LocationType getLocationTypeById(Long id){
        return  mapper.getLocationTypeById(id);
    }

    public Integer getTotalSearchResultCountByLocation(String searchParam) {
        return mapper.getTotalSearchResultCountByLocation(searchParam);
    }

    public Integer getTotalSearchResultCountByLocationType(String searchParam) {
        return mapper.getTotalSearchResultCountByLocationType(searchParam);
    }

    public List<Location> searchBy(String searchParam, String column,  Pagination pagination){
        return mapper.search(searchParam, column, pagination);
    }

    public List<HashMap<String, Object>> getAllLocationsBy(String type) {
        return mapper.getAllLocationsBy(type);
    }
}
