package org.openlmis.core.service;

import org.openlmis.core.domain.Manufacturer;
import org.openlmis.core.repository.ManufactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufactureService {

    @Autowired
    private ManufactureRepository repository;

    public void save(Manufacturer manufacturer) {

        if(manufacturer.getId() == null){
            repository.insert(manufacturer);
        }else {
            repository.update(manufacturer);
        }
    }

    public Manufacturer getByCode(Manufacturer manufacturer) {
        return repository.getByCode(manufacturer.getCode());
    }

}
