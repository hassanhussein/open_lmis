/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.core.repository;

import org.openlmis.core.domain.BudgetLineItem;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.dto.BudgetLineItemDTO;
import org.openlmis.core.message.OpenLmisMessage;
import org.openlmis.core.repository.mapper.BudgetLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

/**
 * BudgetLineItemRepository is Repository class for BudgetLineItem related database operations.
 */

@Repository
public class BudgetLineItemRepository {

  private static final String ERROR_MISSED_FIELD_VALUE = "0";
  @Autowired
  BudgetLineItemMapper mapper;

  @Autowired
  ELMISInterfaceRepository interfaceRepository;
  Map<String, OpenLmisMessage> errors = null;

  public void save(BudgetLineItem budgetLineItem) {
    try {
      mapper.insert(budgetLineItem);
    } catch (DuplicateKeyException e) {
      mapper.update(budgetLineItem);
    }
  }

  public BudgetLineItem get(Long facilityId, Long programId, Long periodId) {
    return mapper.get(facilityId, programId, periodId);
  }

    public BudgetDTO getExistingBudget(Long facilityId) {
    return mapper.getExistingBudget(facilityId);
    }

  public void insertBudget(BudgetDTO budgetDTO) {
      mapper.insertBudget(budgetDTO);
      saveBudgetDetails(budgetDTO.getLineItem(),budgetDTO);
   /*   Long id = mapper.getLineItemById(budgetDTO.getId());
      mapper.deleteBudgetLineItems(budgetDTO.getId());
      saveLineItem(budgetDTO,id);*/
  }


  public void saveBudgetDetails(List<BudgetLineItemDTO> items, BudgetDTO budget) {
     mapper.deleteBudgetLineItems(budget.getId());
    for(BudgetLineItemDTO lineItem : emptyIfNull(items)){

      if (lineItem.getId() == null) {
        lineItem.setId(budget.getId());

        lineItem.setProgramId(budget.getProgramId());
        lineItem.setPeriodId(budget.getPeriodId());
        lineItem.setBudgetFileId(1018L);
        lineItem.setPeriodDate(budget.getReceivedDate());
        lineItem.setBudgetId(budget.getId());
        lineItem.setFacilityId(budget.getFacilityId());

        mapper.insertBudgetLineItem(lineItem);
      } else {

        lineItem.setProgramId(budget.getProgramId());
        lineItem.setPeriodId(budget.getPeriodId());
        lineItem.setBudgetFileId(1018L);
        lineItem.setPeriodDate(budget.getReceivedDate());
        lineItem.setBudgetId(budget.getId());
        lineItem.setFacilityId(budget.getFacilityId());
        mapper.updateBy(lineItem);
      }
    }


  }

  private void saveLineItem(BudgetDTO budgetDTO, Long id) {

      if(id == null) {
        for (BudgetLineItemDTO lineItem  : budgetDTO.getLineItem()) {

          lineItem.setProgramId(budgetDTO.getProgramId());
          lineItem.setPeriodId(budgetDTO.getPeriodId());
          lineItem.setBudgetFileId(1018L);
          lineItem.setPeriodDate(budgetDTO.getReceivedDate());
          lineItem.setBudgetId(budgetDTO.getId());
          lineItem.setFacilityId(budgetDTO.getFacilityId());
          //mapper.deleteBy(budgetDTO.getFacilityId(),budgetDTO.getProgramId(),budgetDTO.getPeriodId());
          mapper.insertBudgetLineItem(lineItem);
        }

      } else {

        for (BudgetLineItemDTO lineItem  : budgetDTO.getLineItem()) {

          lineItem.setProgramId(budgetDTO.getProgramId());
          lineItem.setPeriodId(budgetDTO.getPeriodId());
          lineItem.setBudgetFileId(1018L);
          lineItem.setPeriodDate(budgetDTO.getReceivedDate());
          lineItem.setBudgetId(budgetDTO.getId());
          lineItem.setFacilityId(budgetDTO.getFacilityId());
         // mapper.deleteBy(budgetDTO.getFacilityId(),budgetDTO.getProgramId(),budgetDTO.getPeriodId());
          mapper.updateBy(lineItem);
        }

      }


  }

  public void updateBudget(BudgetDTO budgetDTO) {
    mapper.updateBudget(budgetDTO);
   // mapper.deleteBudgetLineItems(budgetDTO.getId());
/*    Long id = mapper.getLineItemById(budgetDTO.getId());
    saveLineItem(budgetDTO,id);*/
    saveBudgetDetails(budgetDTO.getLineItem(),budgetDTO);
  }

  public void saveLineItemDTO(BudgetLineItemDTO dtos) {
    mapper.deleteBudgetLineItemByFacility(dtos.getFacilityId(),dtos.getProgramId(),dtos.getPeriodId());
    mapper.insertBudgetLineItem(dtos);

  }

  public void updateBudgetInRequisition(Long facilityId, Long programId, Long periodId,String allocatedBudget,String creditValue) {
    mapper.updateBudgetInRequisition(facilityId,programId,periodId,allocatedBudget,creditValue);
  }

    public List<HashMap<String, Object>> getBudgetList() {
          return mapper.getBudgetList();
    }

  public List<HashMap<String, Object>> getBudgetLineItemsList() {

    return mapper.getBudgetLineItemsList();
  }

  public List<HashMap<String, Object>> getSourceOfFundList() {

    return mapper.getSourceOfFundList();
  }
}
