package org.openlmis.core.repository;

import org.openlmis.core.domain.Manufacturer;
import org.openlmis.core.repository.mapper.ManufactureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManufactureRepository {
    @Autowired
    private ManufactureMapper mapper;

    public Integer insert(Manufacturer manufacturer){
        return  mapper.insert(manufacturer);
    }

    public void update(Manufacturer manufacturer){
        mapper.update(manufacturer);
    }

    public Manufacturer getById(Long id){
        return mapper.getById(id);
    }

    public Manufacturer getByCode(String code) {
        return mapper.getByCode(code);
    }
}
