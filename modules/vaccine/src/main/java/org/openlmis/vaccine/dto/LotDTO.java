package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class LotDTO extends BaseModel {

    private Long lotOnHandId;
    private Long productId;
    private Long locationId;
    private Long lotId;
    private Integer quantityOnHand;
    private Integer quantity;
}
