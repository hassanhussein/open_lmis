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
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
      mapper.deleteBudgetLineItems(budgetDTO.getId());
      saveLineItem(budgetDTO);
  }

  private void saveLineItem(BudgetDTO budgetDTO) {

      for (BudgetLineItemDTO lineItem  : budgetDTO.getLineItem()) {
        int leftLimit = 1;
        int rightLimit = 3;
        int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));

        lineItem.setProgramId(1L);
        lineItem.setPeriodId((long) generatedInteger);
        lineItem.setBudgetFileId(1018L);
        lineItem.setPeriodDate(budgetDTO.getReceivedDate());
        lineItem.setBudgetId(budgetDTO.getId());
        lineItem.setFacilityId(budgetDTO.getFacilityId());
        mapper.insertBudgetLineItem(lineItem);
      }

  }

  public void updateBudget(BudgetDTO budgetDTO) {
    mapper.updateBudget(budgetDTO);
    mapper.deleteBudgetLineItems(budgetDTO.getId());
    saveLineItem(budgetDTO);
  }

  public void saveLineItemDTO(BudgetLineItemDTO dtos) {
    mapper.deleteBudgetLineItemByFacility(dtos.getFacilityId());
    mapper.insertBudgetLineItem(dtos);

  }
}
