/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.equipment.service;

import org.openlmis.core.exception.DataException;
import org.openlmis.equipment.domain.EquipmentCategory;
import org.openlmis.equipment.domain.EquipmentType;
import org.openlmis.equipment.repository.EquipmentCategoryRepository;
import org.openlmis.equipment.repository.EquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EquipmentTypeService {

  @Autowired
  private EquipmentTypeRepository repository;

  @Autowired
  private EquipmentCategoryRepository equipmentCategoryRepository;

  public List<EquipmentType> getAll(){
    return repository.getAll();
  }

  public EquipmentType getTypeById(Long id){
    return repository.getEquipmentTypeById(id);
  }

  public void save(EquipmentType type) {
    if(type.getCategory() == null) type.setCategory(new EquipmentCategory());

    populateEquipmentCategory(type);

    if(type.getId() == null){
      repository.insert(type);
    } else {
      repository.update(type);
    }
  }

  public EquipmentType getByCode(EquipmentType type) {
      return repository.getByCode(type.getCode());
    }

  /** used mainly for equipment type upload */
  private void populateEquipmentCategory(EquipmentType type) {
      if(type.getCategory().getId() == null && type.getCategory().getCode() != null) {
        EquipmentCategory category = equipmentCategoryRepository.getByCode(type.getCategory().getCode());
        if(category == null)
          throw new DataException("error.equipment.type.category.data.missing");
        type.setCategory(category);
      }
  }
}
