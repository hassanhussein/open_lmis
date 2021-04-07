package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.EquipmentType;
import org.openlmis.equipment.service.EquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class EquipmentTypePercistenceHandler extends AbstractModelPersistenceHandler {

    EquipmentTypeService equipmentService;

    @Autowired
    public EquipmentTypePercistenceHandler(EquipmentTypeService service) {
        this.equipmentService = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {

        EquipmentType category = (EquipmentType)record;

        return equipmentService.getTypeByCode(category.getCode());
    }

    @Override
    protected void save(BaseModel record) {
        equipmentService.uploadEquipment((EquipmentType) record);
    }
}