package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.EquipmentModel;
import org.openlmis.equipment.service.EquipmentModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class EquipmentModelPercistanceHandler extends AbstractModelPersistenceHandler {

    EquipmentModelService equipmentService;

    @Autowired
    public EquipmentModelPercistanceHandler(EquipmentModelService service) {
        this.equipmentService = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {

        EquipmentModel category = (EquipmentModel)record;

        return equipmentService.getByCode(category.getCode());
    }

    @Override
    protected void save(BaseModel record) {
        equipmentService.uploadEquipment((EquipmentModel) record);
    }
}
