package org.openlmis.core.service;

import org.openlmis.core.domain.LocationType;
import org.openlmis.core.repository.LocationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationTypeService {

    @Autowired
    private LocationTypeRepository repository;

    public void insert(LocationType type) {

        if (type.getId() == null) {

            repository.insert(type);
        }else {
            repository.update(type);
        }

    }

    public LocationType getByCode (LocationType type) {

        if(type.getCode() != null) {
            return repository.getByCode(type.getCode());
        }
        return null;
    }


    public LocationType getBy(String type) {

        if(type != null) {
            return repository.getByCode(type);
        }
        return null;
    }

    public LocationType getByDisplayOrder(Long displayOrder) {
        return repository.getByDisplayOrder(displayOrder);
    }
}
