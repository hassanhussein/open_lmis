package org.openlmis.core.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Manufacturer;
import org.openlmis.core.service.ManufactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ManufacturerPersistenceHandler extends AbstractModelPersistenceHandler {
    ManufactureService service;

    @Autowired
    public ManufacturerPersistenceHandler(ManufactureService service) {
        this.service = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getByCode((Manufacturer)record);
    }

    @Override
    protected void save(BaseModel record) {
        service.save((Manufacturer)record);
    }
}
