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

import org.openlmis.core.domain.Manufacturer;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.Product;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ManufactureService;
import org.openlmis.equipment.domain.*;
import org.openlmis.equipment.dto.EquipmentDTO;
import org.openlmis.equipment.repository.ColdChainEquipmentRepository;
import org.openlmis.equipment.repository.EquipmentRepository;
import org.openlmis.equipment.repository.EquipmentProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class EquipmentService {

  @Autowired
  private EquipmentRepository repository;

  @Autowired
  private EquipmentProductRepository equipmentProductRepository;

  @Autowired
  EquipmentTypeService equipmentTypeService;

  @Autowired
  ColdChainEquipmentRepository coldChainEquipmentRepository;

  @Autowired
  ManufactureService manufactureService;

  @Autowired
  EquipmentEnergyTypeService energyTypeService;
  @Autowired
  EquipmentCategoryService categoryService;

  @Autowired
  private EquipmentModelService equipmentModelService;

  public List<Equipment> getAll(){
      return repository.getAll();
  }

  public List<ColdChainEquipment> getAllCCE(Long equipmentTypeId, Pagination page){
    return coldChainEquipmentRepository.getAll(equipmentTypeId,page);
  }

  public List<Equipment> getAllByType(Long equipmentTypeId) {
    return repository.getAllByType(equipmentTypeId);
  }
  public List<Equipment> getByType(Long equipmentTypeId, Pagination page) {
    return repository.getByType(equipmentTypeId, page);
  }


  public Equipment getById(Long id){
    return repository.getById(id);
  }

  public Equipment getByTypeAndId(Long id,Long equipmentTypeId) {

    EquipmentType equipmentType=equipmentTypeService.getTypeById(equipmentTypeId);

    if (equipmentType.isColdChain()) {
      return coldChainEquipmentRepository.getById(id);
    } else {
      return repository.getById(id);
    }
  }
  public List<EquipmentType> getTypesByProgram(Long programId) {
    return repository.getTypesByProgram(programId);
  }

  public Integer getEquipmentsCountByType(Long equipmentTypeId)
  {
    return repository.getCountByType(equipmentTypeId);
  }

  public Integer getCCECountByType(Long equipmentTypeId)
  {
    return coldChainEquipmentRepository.getCountByType(equipmentTypeId);
  }
  public void saveEquipment(Equipment equipment){
    repository.insert(equipment);
  }

  public void saveEquipmentRelatedProducts(Equipment equipment){
    saveRelatedProducts(equipment);
  }
  public void saveColdChainEquipment(ColdChainEquipment coldChainEquipment){
      coldChainEquipmentRepository.insert(coldChainEquipment);
  }

  public void updateEquipment(Equipment equipment) {
     repository.update(equipment);
  }

  private void saveRelatedProducts(Equipment equipment) {
    equipmentProductRepository.removeAllByEquipmentProducts(equipment.getId());
    if(equipment.getRelatedProducts() != null && equipment.getRelatedProducts().size() > 0){
      for(Product p: equipment.getRelatedProducts()) {
        if(p.getActive()) {
          EquipmentProduct eqp = new EquipmentProduct();
          eqp.setEquipment(equipment);
          eqp.setProduct(p);
          equipmentProductRepository.insert(eqp);
        }
      }
    }
  }

  public void updateColdChainEquipment(ColdChainEquipment coldChainEquipment) {
    coldChainEquipmentRepository.update(coldChainEquipment);
  }


  public void removeEquipment(Long id) {
    repository.remove(id);
  }

  public void removeCCE(Long id) {
    coldChainEquipmentRepository.remove(id);
  }


  public List<ColdChainEquipment>getEquipmentByDesignation(Long designationId){
   return coldChainEquipmentRepository.getEquipmentByDesignation(designationId);
  }

  public List<ColdChainEquipment>getEquipmenentBy(Long equipmentTypeId){
    return coldChainEquipmentRepository.getEquipmentBy(equipmentTypeId);
  }

   public List<NonFunctionalTestTypes> getBioChemistryEquipmentTestTypes() {
      return repository.getBioChemistryEquipmentTestTypes();
   }

  public List<ManualTestTypes> getManualTestTypes() {

    return repository.getManualTestTypes();
  }

  public Equipment getByTypeManufacturerAndModel(Long equipmentTypeId, String manufacturer, Long modelId, String model) {
    return repository.getByTypeManufacturerAndModel(equipmentTypeId, manufacturer, modelId, model);
  }

  public List<Equipment>getManufacturerByEquipmentType(Long equipmentTypeId){
    return coldChainEquipmentRepository.getManufacturerByEquipmentType(equipmentTypeId);
  }

  public List<HashMap<String, Object>>getByModel(Long equipmentTypeId, String manufacturer){
    return coldChainEquipmentRepository.getByModel(equipmentTypeId,manufacturer);
  }

  public EquipmentDTO getByCode(EquipmentDTO code) {
    return repository.getByCode(code.getCode());
  }

  public void uploadEquipment(EquipmentDTO record) {

    EquipmentType type = equipmentTypeService.getTypeByCode(record.getEquipmentType().getCode());
    Manufacturer manufacturer = manufactureService.getByCode(record.getFacturer());
    EquipmentEnergyType energyType = energyTypeService.getByCode(record.getEnergyType().getCode());
    EquipmentModel model = equipmentModelService.getByCode(record.getEquipmentModel().getCode());
    EquipmentCategory category = categoryService.getByCode(record.getEquipmentCategory().getCode());
    if(category != null && type != null && manufacturer !=null && energyType != null&& model != null) {

      Equipment e = new Equipment();
      e.setEquipmentType(type);
      e.setEquipmentTypeId(type.getId());
      e.setManufacturer(manufacturer.getName());
      e.setManufacturerId(manufacturer.getId());
      e.setModel(model.getName());
      e.setModelId(model.getId());
      e.setEnergyTypeId(energyType.getId());
      e.setEquipmentDTO(record);
      e.setCreatedBy(1L);
      e.setModifiedBy(1L);
      e.setName(record.getName());
      e.setCode(record.getCode());
      e.setEnergyType(energyType);
      e.setEquipmentModel(model);
      e.setEquipmentCategoryId(category.getId());

      if(record.getId() == null) {
        repository.insert(e);
      } else {
        e.setId(record.getId());
        repository.update(e);
      }

    } else {
       throw new DataException("Empty Manufacturer or Equipment or Energy");
    }

  }

    public List<Equipment> getAllByTypeCategory(Long equipmentTypeId, Long equipmentCategoryId) {
    return repository.getAllByTypeCategory(equipmentTypeId,equipmentCategoryId);
    }
}
