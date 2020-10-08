package org.openlmis.equipment.domain;

import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discipline extends BaseModel implements Importable {

    @ImportField(type = "String", name = "Code")
    private String code;

    @ImportField(type = "String", name = "Name")
    private String name;

    @ImportField(type = "String", name = "Description")
    private String description;

}
