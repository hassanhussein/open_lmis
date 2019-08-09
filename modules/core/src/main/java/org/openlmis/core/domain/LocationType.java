package org.openlmis.core.domain;

import lombok.Data;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
public class LocationType extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Location Type Code")
    private String code;

    @ImportField(mandatory = true, name = "Location Type Name")
    private String name;

    @ImportField(name = "Description")
    private String description;

    @ImportField(name = "Active")
    private boolean active;

}
