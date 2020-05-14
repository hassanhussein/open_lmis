package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.vaccine.domain.wms.dto.LotOnHandDTO;
import org.openlmis.vaccine.domain.wms.dto.LotOnHandLocationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hassan on 9/10/17.
 */
@Data
public class StockOnHandSummaryDTO extends BaseModel {

    private String product;

    private String productCode;

    private Long productId;

    private List<LotOnHandDTO> lots = new ArrayList<>();

}
