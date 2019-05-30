package org.openlmis.equipment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.annotation.ImportField;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class EquipmentCategory extends BaseModel {

    @ImportField(mandatory = true, name = "Equipment Category Code")
    String code;

    @ImportField(mandatory = true, name = "Equipment Category Name")
    String name;

    List<Long> functionalTestTypeIds;

    List<Long> equipmentTypeIds;

}