/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */


package org.openlmis.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.openlmis.core.domain.EDIFileColumn;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.message.OpenLmisMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * BudgetLineItemDTO is Data transfer object for BudgetLineItems, consolidates user provided information like
 * facilityCode, programCode etc., to be later referenced using Ids in BudgetLineItem.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = NON_NULL)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class BudgetLineItemDTO {

  @JsonProperty("CustID")
  private String facilityCode;
  private String programCode;
  private String periodStartDate;

  @JsonProperty("Balance")
  private String allocatedBudget;
  private String notes;
  private String fundSourceCode;
  private Boolean additive;
  @JsonProperty("TranDate")
  private String periodDate;
  private Long periodId;
  private Long facilityId;
  private Long budgetFileId;
  private Long budgetId;

  private Long programId;

  private static Logger logger = LoggerFactory.getLogger(BudgetLineItemDTO.class);

  public static BudgetLineItemDTO populate(List<String> fieldsInOneRow, Collection<EDIFileColumn> budgetFileColumns) {
    BudgetLineItemDTO lineItemDTO = new BudgetLineItemDTO();
    for (EDIFileColumn budgetFileColumn : budgetFileColumns) {
      Integer position = budgetFileColumn.getPosition();
      String name = budgetFileColumn.getName();
      try {
        Field field = BudgetLineItemDTO.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(lineItemDTO, fieldsInOneRow.get(position - 1).trim());
      } catch (Exception e) {
        logger.error("Unable to set field '" + name +
            "' in BudgetLinetItemDTO, check mapping between DTO and BudgetFileColumn", e);
      }
    }
    return lineItemDTO;
  }

  public void checkMandatoryFields() {
    if (isBlank(this.facilityCode) || isBlank(this.programCode) || isBlank(this.allocatedBudget) || isBlank(this.periodStartDate))
      throw new DataException(new OpenLmisMessage("error.mandatory.fields.missing"));
  }

}
