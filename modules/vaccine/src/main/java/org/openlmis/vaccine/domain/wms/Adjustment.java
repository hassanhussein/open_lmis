package org.openlmis.vaccine.domain.wms;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class Adjustment extends BaseModel {
    private Long lotId,locationid,productId,vvmId,stockCardId;
    private Integer quantity;
    private String reason,type;

}
