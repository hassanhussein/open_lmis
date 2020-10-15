package org.openlmis.equipment.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.equipment.domain.Discipline;
import org.openlmis.equipment.service.EquipmentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DisciplinePercistenceHandler extends AbstractModelPersistenceHandler {

    EquipmentCategoryService service;

    @Autowired
    public DisciplinePercistenceHandler(EquipmentCategoryService service) {
        this.service = service;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return service.getDisciplineByCode((Discipline)record);
    }

    @Override
    protected void save(BaseModel record) {
        service.saveDiscipline((Discipline) record);
    }
}
