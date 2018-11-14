package org.openlmis.core.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.FacilityMappingDTO;
import org.openlmis.core.service.ELMISInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FacilityInterfaceMappingHandler extends AbstractModelPersistenceHandler {

    private ELMISInterfaceService interfaceService;

    @Autowired
    FacilityInterfaceMappingHandler(ELMISInterfaceService service) {
        this.interfaceService = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return interfaceService.getFacilityMappingByCode((FacilityMappingDTO) record);
    }

    @Override
    protected void save(BaseModel record) {
        interfaceService.insertFacility((FacilityMappingDTO) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.program.supported";
    }
}
