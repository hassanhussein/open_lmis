package org.openlmis.core.dto;

import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SourceOfFundDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "Code")
    private String code;

    @ImportField(mandatory = true, name = "Source Name")
    private String name;

    @ImportField(name = "Display Order")
    private Integer displayOrder;

}
