package org.openlmis.core.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.FacilityGeoLocationDTO;
import org.openlmis.core.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FacilityGeoLocationPersistenceHandler extends AbstractModelPersistenceHandler{
   FacilityService facilityService;

   @Autowired
    FacilityGeoLocationPersistenceHandler(FacilityService facilityService){

       this.facilityService = facilityService;
   }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return facilityService.getGeoLocationFacilities((FacilityGeoLocationDTO)record);
    }

    @Override
    protected void save(BaseModel record) {
         facilityService.saveFacilityGeoLocation(((FacilityGeoLocationDTO)record));
    }
}
