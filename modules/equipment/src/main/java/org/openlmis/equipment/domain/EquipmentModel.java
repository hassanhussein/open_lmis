package org.openlmis.equipment.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentModel extends BaseModel implements Importable {

    @ImportField(type = "String", name = "Equipment type Code", nested = "code")
    EquipmentType equipmentType;

    Long equipmentTypeId;
    @ImportField(type = "String", name = "Name")
    String name;

    @ImportField(type = "String", name = "Code")
    String code;
}
