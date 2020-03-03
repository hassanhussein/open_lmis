package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.dto.EquipmentDTO;
import org.openlmis.equipment.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UploadEquipmentPercistenceHandler extends AbstractModelPersistenceHandler {

    EquipmentService equipmentService;

    @Autowired
    public UploadEquipmentPercistenceHandler(EquipmentService service) {
        this.equipmentService = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return equipmentService.getByCode((EquipmentDTO)record);
    }

    @Override
    protected void save(BaseModel record) {
       equipmentService.uploadEquipment((EquipmentDTO) record);
    }
}
