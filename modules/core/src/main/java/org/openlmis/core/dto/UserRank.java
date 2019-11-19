package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
public class UserRank  extends BaseModel implements Importable {

    @ImportField(name = "code")
    private String code;
    @ImportField(name = "name")
    private String name;
    @ImportField(name = "displayOrder")
    private Long displayOrder;
}
