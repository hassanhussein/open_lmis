package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.LocationType;
import org.openlmis.core.service.LocationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationTypePersistenceHandler extends AbstractModelPersistenceHandler {

    LocationTypeService service;

    @Autowired
    public LocationTypePersistenceHandler(LocationTypeService service) {
        this.service = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getByCode((LocationType) record);
    }

    @Override
    protected void save(BaseModel record) {
        service.insert((LocationType) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.location.type.code";
    }


}
