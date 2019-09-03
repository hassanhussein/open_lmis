package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
public class FacilityDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Facility Code")
    private String code;

    @ImportField(type = "double", name = "Facility LAT")
    private Double latitude;

    @ImportField(type = "double", name = "Facility LONG")
    private Double longitude;
}
