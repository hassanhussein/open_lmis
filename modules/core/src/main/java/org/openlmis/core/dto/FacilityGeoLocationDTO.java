package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
public class FacilityGeoLocationDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Location code")
    private String code;

    @ImportField(mandatory = true, name = "Location name")
    private String name;

}
