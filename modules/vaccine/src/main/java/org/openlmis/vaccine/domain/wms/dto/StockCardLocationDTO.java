package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class StockCardLocationDTO extends BaseModel {

    private Long locationId;

    private Long stockCardId;
}
