package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
public class AdjustmentReasonExDTO extends BaseModel implements Importable {

    @ImportField(name="code", mandatory = true)
    private String code;
    @ImportField(name="Reason Name", mandatory = true)
    private String reasonName;

}
