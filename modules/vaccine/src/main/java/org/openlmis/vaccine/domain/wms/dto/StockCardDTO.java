package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class StockCardDTO extends BaseModel {

    private String product;

    private Long productId;

    private Integer quantityOnHand;

    private String productCode;
}
