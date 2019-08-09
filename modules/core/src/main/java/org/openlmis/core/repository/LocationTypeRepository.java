package org.openlmis.core.repository;

import org.openlmis.core.domain.LocationType;
import org.openlmis.core.repository.mapper.LocationTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationTypeRepository {

@Autowired
private LocationTypeMapper mapper;


public Integer insert(LocationType type) {
    return mapper.insert(type);
}

public void update(LocationType type) {
    mapper.update(type);
}

public LocationType getByCode (String code){
   return mapper.getByCode(code);
}


}
