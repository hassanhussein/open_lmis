package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.Equipment;
import org.openlmis.equipment.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EquipmentPersistenceHandler is used for uploads of Equipments
 * It uploads each equipments data record by record.
 */
@Component
@NoArgsConstructor
public class EquipmentPersistenceHandler extends AbstractModelPersistenceHandler {

    @Autowired
    EquipmentService service;

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getExistingEquipmentForUpload((Equipment) record);
    }

    @Override
    protected void save(BaseModel record) {
        Equipment equipment = service.validateAndLoadEquipmentUploadAttributes((Equipment)record);
        if(equipment.getId() == null)
            service.saveEquipment(equipment);
        else
            service.updateEquipment(equipment);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.equipment";
    }
}
