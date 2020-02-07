package org.openlmis.lookupapi.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.lookupapi.model.HealthFacilityDTO;
import org.openlmis.lookupapi.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class HfrUploadPersistenceHandler extends AbstractModelPersistenceHandler {
   private LookupService lookupService;

   @Autowired
   public HfrUploadPersistenceHandler(LookupService lookupService){
       this.lookupService = lookupService;

   }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return lookupService.getByHfrCode((HealthFacilityDTO)record);
    }

    @Override
    protected void save(BaseModel record) {
       lookupService.saveHFR((HealthFacilityDTO)record);
    }
}
