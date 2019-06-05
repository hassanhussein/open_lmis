package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.dto.EquipmentInventoryUploadDto;
import org.openlmis.equipment.service.EquipmentInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EquipmentInventoryPersistenceHandler is used for uploads of Equipment Inventory
 * It uploads each equipment Inventor data record by record.
 */
@Component
@NoArgsConstructor
public class EquipmentInventoryPersistenceHandler extends AbstractModelPersistenceHandler {

    @Autowired
    EquipmentInventoryService service;

    @Override
    protected BaseModel getExisting(BaseModel record) {
       return service.getExistingInventoryForUpload((EquipmentInventory) record);
    }

    @Override
    protected void save(BaseModel record) {
        service.uploadEquipmentInventory((EquipmentInventory) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.equipment.inventory";
    }

}
