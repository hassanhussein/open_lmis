package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
public class HfrMappingDTO extends BaseModel  implements Importable {

    @ImportField(mandatory = true, name = "code")
    private String code;

    private String hfrDistrict;
    @ImportField(type = "boolean", mandatory = true, name = "Is Active")
    private Boolean active;
    private Long vimsDistrict;

    @ImportField(mandatory = true, name = "HFR District Code")
    private String hfrCode;

    @ImportField(name = "HFR District Name")
    private String description;

}
