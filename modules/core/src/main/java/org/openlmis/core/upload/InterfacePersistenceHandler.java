package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.ELMISInterfaceFacilityMappingDTO;
import org.openlmis.core.service.ELMISInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterfacePersistenceHandler extends AbstractModelPersistenceHandler {
   @Autowired
    ELMISInterfaceService service;

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getELMISInterface((ELMISInterfaceFacilityMappingDTO)record);
    }

    @Override
    protected void save(BaseModel record) {
        service.uploadELMISInterface((ELMISInterfaceFacilityMappingDTO)record);
    }
}
