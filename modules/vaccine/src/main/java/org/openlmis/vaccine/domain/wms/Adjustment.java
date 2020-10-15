package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.stockmanagement.domain.StockCardEntryKV;

import java.util.List;

@Data
public class Adjustment extends BaseModel {
    private Long lotId,locationid,productId,vvmId,toVvmId,stockCardId;
    private Integer quantity;
    private String reason,type;
    private Long toWarehouseId;
    private Long toBinId;
    private  Boolean isTransfer;

    private  String transferLogs;

    @JsonIgnore
    private List<StockCardEntryKV> keyValues;

}
