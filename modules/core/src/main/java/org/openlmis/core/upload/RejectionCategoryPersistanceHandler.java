package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.RejectionCategoryDTO;
import org.openlmis.core.service.RejectionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RejectionCategoryPersistanceHandler extends AbstractModelPersistenceHandler{

    @Autowired
    RejectionCategoryService service;

    @Autowired
    private RejectionCategoryPersistanceHandler(RejectionCategoryService service) {
        this.service = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getByCode((RejectionCategoryDTO) record);
    }

    @Override
    protected void save(BaseModel record) {
        service.save((RejectionCategoryDTO) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.rejection.reason.code";
    }
}
