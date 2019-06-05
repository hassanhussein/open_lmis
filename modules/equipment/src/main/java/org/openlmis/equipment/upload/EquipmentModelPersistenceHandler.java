package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.EquipmentModel;
import org.openlmis.equipment.service.EquipmentModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EquipmentModelPersistenceHandler is used for uploads of Equipment models
 * It uploads each equipment model data record by record.
 */
@Component
@NoArgsConstructor
public class EquipmentModelPersistenceHandler extends AbstractModelPersistenceHandler {

    @Autowired
    EquipmentModelService service;

    @Override
    protected BaseModel getExisting(BaseModel record)  {
        return service.getByCode((EquipmentModel) record);
    }

    @Override
    protected void save(BaseModel record) {
        service.uploadEquipmentModel((EquipmentModel) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.equipment.model";
    }
}
