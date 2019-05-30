package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.EquipmentType;
import org.openlmis.equipment.service.EquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EquipmentTypePersistenceHandler is used for uploads of Equipment types
 * It uploads each equipment type data record by record.
 */
@Component
@NoArgsConstructor
public class EquipmentTypePersistenceHandler extends AbstractModelPersistenceHandler {

    @Autowired
    EquipmentTypeService service;

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getByCode((EquipmentType) record);
    }

    @Override
    protected void save(BaseModel record) { service.save((EquipmentType) record); }

    @Override
    public String getMessageKey() {
        return "error.duplicate.equipment.type";
    }
}
