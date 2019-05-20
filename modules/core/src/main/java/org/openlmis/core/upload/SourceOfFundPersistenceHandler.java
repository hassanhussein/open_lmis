package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.core.service.SourceOfFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceOfFundPersistenceHandler  extends AbstractModelPersistenceHandler{


    private SourceOfFundService sourceOfFundService;

    @Autowired
    public SourceOfFundPersistenceHandler(SourceOfFundService sourceOfFundService) {
        this.sourceOfFundService = sourceOfFundService;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return sourceOfFundService.getExisting((SourceOfFundDTO) record);
    }

    @Override
    protected void save(BaseModel record) {
        sourceOfFundService.save((SourceOfFundDTO) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.source.name";
    }


}
