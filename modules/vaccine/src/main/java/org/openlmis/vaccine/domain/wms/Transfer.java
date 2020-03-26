package org.openlmis.vaccine.domain.wms;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.vaccine.dto.LocationDTO;

import java.util.Date;

@Data
public class Transfer extends BaseModel {

    private WareHouse fromWarehouseId;

    private WareHouse toWarehouseId;

    private LocationDTO fromBin;

    private LocationDTO toBin;

    private Long productId;

    private Date transferDate;

    private Long reasonId;

    private String lotNumber;

    private Integer quantity;

    private String remarks;

    private Boolean notify;



}
