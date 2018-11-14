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
public class FacilityMappingDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Facility Code")
    private String mappedId;
    private Boolean active;
    @ImportField(mandatory = true, name = "eLMIS Facility Code")
    private String facilityCode;
    private Long facilityId;
    private Long interfaceId;
    private String interfaceCode;

}
