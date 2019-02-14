package org.openlmis.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BudgetLineItem;
import org.openlmis.core.exception.DataException;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BudgetDTO {

    private String facilityCode;
    private String programCode;
    private String periodStartDate;
    private List<BudgetLineItemDTO> lineItem = new ArrayList<>();

    public void validate() {
        if (isBlank(facilityCode) || isBlank(periodStartDate)) {
            throw new DataException("error.mandatory.fields.missing");
        }
        for (BudgetLineItemDTO lineItem : lineItem) {
            lineItem.checkMandatoryFields();
        }
    }
}
