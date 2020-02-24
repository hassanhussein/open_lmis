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
public class ReceiveLot extends BaseModel {

    private Integer locationId;
    private String lotNumber;
    private String serialNumber;
    private Date expiryDate;
    private Date manufacturingDate;
    private Integer quantity;
    private ReceiveLineItem receiveLineItem;
    private String manufacturerName;

    private Location location;

    private String packSize;

    private String boxNumber;

}
