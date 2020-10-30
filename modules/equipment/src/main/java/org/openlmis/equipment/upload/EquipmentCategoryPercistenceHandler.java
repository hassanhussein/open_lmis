package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.EquipmentCategory;
import org.openlmis.equipment.service.EquipmentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class EquipmentCategoryPercistenceHandler extends AbstractModelPersistenceHandler {

    EquipmentCategoryService equipmentService;

    @Autowired
    public EquipmentCategoryPercistenceHandler(EquipmentCategoryService service) {
        this.equipmentService = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {

        EquipmentCategory category = (EquipmentCategory)record;

        return equipmentService.getByCode(category.getCode());
    }

    @Override
    protected void save(BaseModel record) {
        equipmentService.uploadEquipment((EquipmentCategory) record);
    }
}
