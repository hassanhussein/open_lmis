package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.FacilityDTO;
import org.openlmis.core.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacilityGeoCordinatesPersistenceHandler  extends AbstractModelPersistenceHandler {

    FacilityService  service;

    @Autowired
    public FacilityGeoCordinatesPersistenceHandler(FacilityService service) {
        this.service = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getFacilityForCode((FacilityDTO)record);
    }

    @Override
    protected void save(BaseModel record) {
       service.updateCordinates((FacilityDTO)record);
    }
}
