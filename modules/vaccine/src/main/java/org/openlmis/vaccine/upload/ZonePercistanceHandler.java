package org.openlmis.vaccine.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.vaccine.domain.wms.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.openlmis.vaccine.service.warehouse.ZoneService;


@Component
@NoArgsConstructor
public class ZonePercistanceHandler extends AbstractModelPersistenceHandler {

     @Autowired
     private ZoneService service;

    public ZoneService getService(ZoneService zoneService) {
        return this.service = zoneService;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getByCode((Zone)record);
    }

    @Override
    protected void save(BaseModel record) {
        service.save((Zone) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.infant.mortality.rate";
    }
}
