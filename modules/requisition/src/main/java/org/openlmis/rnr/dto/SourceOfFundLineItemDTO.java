package org.openlmis.rnr.dto;

import lombok.*;
import org.openlmis.core.domain.BaseModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SourceOfFundLineItemDTO extends BaseModel {

    private Long rnrId;

    private BigDecimal allocatedBudget;

}
