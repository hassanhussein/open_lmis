package org.openlmis.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ELMISInterfaceExtDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "eLMIS Interface Code")
    private String interfaceId;

    @ImportField(mandatory = true, name = "Facility Code")
    private String facilityCode;

    @ImportField(mandatory = true, name = "Mapped Facility Code")
    private String mappedId;

    @ImportField(mandatory = true, type = "boolean", name = "Facility Is Active")
    private Boolean active;

}
