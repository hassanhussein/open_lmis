package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zone extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Zone Code")
    private String code;

    @ImportField(mandatory = true, name = "Zone Name")
    private String name;

    private String description;

}
