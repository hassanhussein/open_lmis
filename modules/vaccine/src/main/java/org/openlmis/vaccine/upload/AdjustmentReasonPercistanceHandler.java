package org.openlmis.vaccine.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;


import org.openlmis.vaccine.dto.AdjustmentReasonExDTO;
import org.openlmis.vaccine.service.warehouse.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdjustmentReasonPercistanceHandler extends AbstractModelPersistenceHandler {

    @Autowired
    private TransferService service;

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getAllAdjumentReasons((AdjustmentReasonExDTO)record);
    }

    @Override
    protected void save(BaseModel record) {
        service.insertReason((AdjustmentReasonExDTO)record);
    }
}
