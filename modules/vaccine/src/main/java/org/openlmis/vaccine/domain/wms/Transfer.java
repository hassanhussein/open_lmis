package org.openlmis.vaccine.domain.wms;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.vaccine.dto.LocationDTO;

import java.util.Date;

@Data
public class Transfer extends BaseModel {

    private Long fromWarehouseId;

    private Long toWarehouseId;

    private Long fromBin;

    private Long toBin;

    private Long productId;

    private Date transferDate;

    private String reason;

    private Long lotId;

    private Integer quantity;

    private String remarks;

    private Boolean notify;

    private Long stockCardId;



}
