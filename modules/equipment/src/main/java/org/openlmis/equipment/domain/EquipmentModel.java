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

    @ImportField(mandatory = true, name = "Equipment Type", nested = "code")
    EquipmentType equipmentType;

    Long equipmentTypeId;

    @ImportField(mandatory = true, name = "Equipment Model Name")
    String name;

    @ImportField(mandatory = true, name = "Equipment Model Code")
    String code;
}
