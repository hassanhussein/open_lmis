/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.equipment.service;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.exception.DataException;
import org.openlmis.equipment.domain.EquipmentModel;
import org.openlmis.equipment.domain.EquipmentType;
import org.openlmis.equipment.repository.EquipmentModelRepository;
import org.openlmis.equipment.repository.EquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class EquipmentModelService {

    @Autowired
    EquipmentModelRepository repository;

    @Autowired
    EquipmentTypeService equipmentTypeService;

    public List<EquipmentModel> getAll() {
        return repository.getAll();
    }

    public EquipmentModel getEquipmentModelById(Long id) {
        return repository.getEquipmentModelById(id);
    }

    public void deleteEquipmentModel(Long id) {
        repository.deleteEquipmentModel(id);
    }

    public void updateEquipmentModel(EquipmentModel obj) {
        repository.updateEquipmentModel(obj);
    }

    public void insertEquipmentModel(EquipmentModel obj) {
        repository.insertEquipmentModel(obj);
    }

    public List<EquipmentModel> getByEquipmentTypeId(Long equipmentTypeId){
        return repository.getByEquipmentTypeId(equipmentTypeId);
    }

    public EquipmentModel getByCode(EquipmentModel model) {
        return repository.getByCode(model.getCode());
    }

    public void uploadEquipmentModel(EquipmentModel model) {
        EquipmentType equipmentType = equipmentTypeService.getByCode(model.getEquipmentType());

        if(equipmentType == null)
            throw new DataException("equipment.type.missing.data");

        model.setEquipmentTypeId(equipmentType.getId());
        model.setCreatedDate(new Date());

        if(model.getId() != null)
            updateEquipmentModel(model);
        else
            insertEquipmentModel(model);
    }
}
