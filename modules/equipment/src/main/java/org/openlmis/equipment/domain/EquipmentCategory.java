package org.openlmis.equipment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class EquipmentCategory extends BaseModel implements Importable {

    @ImportField(type = "String", name = "Code")
    String code;

    @ImportField(type = "String", name = "Name")
    String name;

    List<Long> functionalTestTypeIds;

    List<Long> equipmentTypeIds;

    Long disciplineId;

    @ImportField(type = "String", name = "Discipline Code", nested = "code")
    Discipline discipline;

}