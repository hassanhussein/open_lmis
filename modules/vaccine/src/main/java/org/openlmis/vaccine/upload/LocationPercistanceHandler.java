package org.openlmis.vaccine.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.vaccine.dto.LocationDTO;
import org.openlmis.vaccine.service.warehouse.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class LocationPercistanceHandler extends AbstractModelPersistenceHandler {

    private WareHouseService wareHouseService;

    @Autowired
    public LocationPercistanceHandler(WareHouseService wareHouseService) {
        this.wareHouseService = wareHouseService;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return wareHouseService.getBy((LocationDTO)record);
    }

    @Override
    protected void save(BaseModel record) {
       wareHouseService.saveLocations((LocationDTO)record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.infant.mortality.rate";
    }
}
