package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveDetail extends BaseModel {

    private Integer receiveId;
    private Integer productId;
    private Date expiryDate;
    private Date manufacturingDate;
    private Integer quantityCounted;
    private Double unitPrice;
    private Integer boxCounted;
    private boolean lotFlag;
    private Receive receive;

}
