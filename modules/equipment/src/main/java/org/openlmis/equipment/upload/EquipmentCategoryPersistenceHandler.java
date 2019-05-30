package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.EquipmentCategory;
import org.openlmis.equipment.service.EquipmentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EquipmentCategoryPersistenceHandler is used for uploads of Equipment category
 * It uploads each equipment category data record by record.
 */
@Component
@NoArgsConstructor
public class EquipmentCategoryPersistenceHandler extends AbstractModelPersistenceHandler {

    @Autowired
    EquipmentCategoryService equipmentCategoryService;

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return equipmentCategoryService.getByCode((EquipmentCategory) record);
    }

    @Override
    protected void save(BaseModel record) { equipmentCategoryService.save((EquipmentCategory) record);  }

    @Override
    public String getMessageKey() {
        return "error.duplicate.equipment.category";
    }
}
