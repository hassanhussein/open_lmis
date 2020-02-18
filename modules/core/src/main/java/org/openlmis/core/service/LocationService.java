package org.openlmis.core.service;

import org.openlmis.core.domain.Location;
import org.openlmis.core.domain.LocationType;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    LocationRepository repository;

    @Transactional
    public void save(Location location){

        repository.save(location);
    }
    public List<Location> getAllLocations(){

        return repository.getAllLocations();
    }
    public Location getById(Long id){
        return repository.getById(id);
    }

    public Location getBycode(String code){

        return repository.getByCode(code);
    }
    public void deleteById(Long id){
        repository.deleteById(id);
    }
    public List<LocationType> getAllLocationTypes(){

        return  repository.getAllLocationTypes();
    }
    public Integer getTotalSearchResultCount(String searchParam, String column) {
        if(column.equals("name")){
            return repository.getTotalSearchResultCountByLocation(searchParam);
        }
        if(column.equals("type")){
        return repository.getTotalSearchResultCountByLocationType(searchParam);
        }
        return 0;
    }

    public List<Location> searchBy(String searchParam, String column, Pagination pagination) {
        return repository.searchBy(searchParam, column, pagination);
    }

    public List<HashMap<String, Object>> getAllLocationsBy(String type) {
        return  repository.getAllLocationsBy(type);

    }
}
