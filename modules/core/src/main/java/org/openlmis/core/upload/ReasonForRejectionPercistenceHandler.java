package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.dto.RejectionReasonDTO;
import org.openlmis.core.service.RejectionReasonDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReasonForRejectionPercistenceHandler extends AbstractModelPersistenceHandler {

    @Autowired
    RejectionReasonDTOService service;

    @Autowired
    private ReasonForRejectionPercistenceHandler(RejectionReasonDTOService service) {
        this.service = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getByCode((RejectionReasonDTO) record);
    }

    @Override
    protected void save(BaseModel record) {
        service.insert((RejectionReasonDTO) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.rejection.reason.code";
    }

}
