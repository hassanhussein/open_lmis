package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.LocationType;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;
import org.openlmis.vaccine.domain.wms.WareHouse;

@Data
public class LocationDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "code")
    private String code;

    @ImportField(mandatory = true, name = "name")
    private String name;

    private WareHouse house;

    private LocationType type;

    private Long typeId;

    private Long warehouseId;

    private String warehouseName;

    private String locationType;

    @ImportField(mandatory = true, name = "warehouse Code")
    private String warehouseCode;

    @ImportField(mandatory = true, name = "location Type Code")
    private String locationTypeCode;

}
