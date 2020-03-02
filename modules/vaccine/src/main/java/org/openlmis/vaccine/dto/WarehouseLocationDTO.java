package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.vaccine.domain.wms.Site;

import java.util.List;

@Data
public class WarehouseLocationDTO extends BaseModel {

    String name;

    String code;

    Site site;

    List<LocationDTO> locations;

}
