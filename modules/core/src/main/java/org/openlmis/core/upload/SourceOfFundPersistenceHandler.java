package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.core.service.SourceOfFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceOfFundPersistenceHandler  extends AbstractModelPersistenceHandler{
   @Autowired

   SourceOfFundService sourceOfFundService;

    @Autowired
    public SourceOfFundPersistenceHandler(SourceOfFundService sourceOfFundService) {
        this.sourceOfFundService = sourceOfFundService;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return sourceOfFundService.getExisting((SourceOfFundDTO) record);
    }

    @Override
    protected void save(BaseModel modelClass) {
        sourceOfFundService.save((SourceOfFundDTO) modelClass);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.product.category";
    }


}
