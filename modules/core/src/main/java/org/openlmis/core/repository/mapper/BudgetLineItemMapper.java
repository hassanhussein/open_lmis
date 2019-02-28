/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.BudgetLineItem;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.dto.BudgetLineItemDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BudgetLineItemMapper maps the BudgetLineItem entity to corresponding representation in database.
 */
@Repository
public interface BudgetLineItemMapper {

  @Insert({
    "INSERT INTO budget_line_items (facilityId, programId, budgetFileId, periodId, periodDate, allocatedBudget, notes) ",
    "VALUES (#{facilityId}, #{programId}, #{budgetFileId}, #{periodId}, #{periodDate}, #{allocatedBudget}, #{notes})"
  })
  @Options(useGeneratedKeys = true)
  void insert(BudgetLineItem budgetLineItem);

  @Update({
    "UPDATE budget_line_items SET budgetFileId = #{budgetFileId}, periodDate = #{periodDate}, allocatedBudget = #{allocatedBudget}, notes = #{notes} ",
    "WHERE id = #{id}"
  })
  void update(BudgetLineItem budgetLineItem);

  @Select({
    "SELECT * FROM budget_line_items WHERE facilityId = #{facilityId} AND programId = #{programId} AND periodId = #{periodId}"
  })
  BudgetLineItem get(@Param("facilityId") Long facilityId, @Param("programId") Long programId, @Param("periodId") Long periodId);

  @Select(" SELECT * FROM budgets WHERE facilityId = #{facilityId} limit 1 ")
  BudgetDTO getExistingBudget(@Param("facilityId") Long facilityId);

  @Insert("INSERT INTO public.budgets(\n" +
          "             createdBy, createdDate, modifiedBy, modifiedDate, facilityId, \n" +
          "            sourceApplication, receivedDate)\n" +
          "    VALUES (#{createdBy}, now(), #{modifiedBy}, NOW(), #{facilityId}, #{sourceApplication},#{receivedDate}::date);")
  @Options(useGeneratedKeys = true)
  Integer insertBudget(BudgetDTO budgetDTO);

  @Insert({
          "INSERT INTO budget_line_items (facilityId, programId, budgetFileId, periodId, periodDate, allocatedBudget, notes,budgetId,additive) ",
          "VALUES (#{facilityId}, #{programId}, #{budgetFileId}, #{periodId}, #{periodDate}::date,  " +
                  " CAST (#{allocatedBudget} AS DOUBLE PRECISION) " +
                  " , #{notes},#{budgetId},#{additive})"
  })
  @Options(useGeneratedKeys = true)
    void insertBudgetLineItem(BudgetLineItemDTO lineItem);

  @Delete(" delete from budget_line_items where budgetid = #{budgetId} ")
   void deleteBudgetLineItems(@Param("budgetId")Long budgetId);

  @Update("UPDATE public.budgets\n" +
          "   SET createdBy=#{createdBy}, createdDate=NOW(), modifiedBy= #{modifiedBy}, modifiedDate=NOW(), sourceApplication=#{sourceApplication}, receivedDate=#{receivedDate}::date\n" +
          " WHERE id = #{id}; ")
  void updateBudget(BudgetDTO budgetDTO);

  @Delete("delete from budget_line_items where facilityId = #{facilityId}")
    void deleteBudgetLineItemByFacility(@Param("facilityId") Long facilityId,
                                        @Param("programId") Long programId,
                                        @Param("periodId") Long periodId);

  @Update("update requisitions set allocatedBudget = CAST (#{allocatedBudget} AS DOUBLE PRECISION) where facilityId = #{facilityId} and " +
          " programId = #{programId} and periodId = #{periodId} ")
  void updateBudgetInRequisition(@Param("facilityId") Long facilityId,
                                 @Param("programId") Long programId,
                                 @Param("periodId") Long periodId,
                                 @Param("allocatedBudget") String allocatedBudget);
}
