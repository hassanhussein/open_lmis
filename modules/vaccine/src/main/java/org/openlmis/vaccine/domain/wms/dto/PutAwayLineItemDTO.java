package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class PutAwayLineItemDTO extends BaseModel {

    private Long lotId;

    private Long inspectionId;

    private Integer quantity;

    private Long fromWareHouseId;

    private Long toWareHouseId;

    private Long fromBinLocationId;

    private Long toBinLocationId;

    private Long productId;

}
