package org.openlmis.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = NON_EMPTY)
public class InBoundLineItemDTO extends BaseModel implements Importable {

    private Long inBoundId;

    private String productCode;

    private String productName;

    private String uom;

    private Long quantityOrdered;

    private String source;

    private Long fundValues;

}
