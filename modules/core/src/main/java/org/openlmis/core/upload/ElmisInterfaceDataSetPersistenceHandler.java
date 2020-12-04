package org.openlmis.core.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.ELMISInterfaceDataSet;
import org.openlmis.core.service.ELMISInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ElmisInterfaceDataSetPersistenceHandler extends AbstractModelPersistenceHandler{

    private ELMISInterfaceService elmisInterfaceService;

    @Autowired
    ElmisInterfaceDataSetPersistenceHandler(ELMISInterfaceService elmisInterfaceService){
        this.elmisInterfaceService = elmisInterfaceService;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return elmisInterfaceService.getElmisInterfaceProductCodeAndInterfaceId((ELMISInterfaceDataSet) record);
    }

    @Override
    protected void save(BaseModel record) {
        elmisInterfaceService.saveUploadedDataSet((ELMISInterfaceDataSet)record);
    }
}
