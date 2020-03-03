package org.openlmis.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Manufacturer;
import org.openlmis.equipment.domain.EquipmentCategory;
import org.openlmis.equipment.domain.EquipmentEnergyType;
import org.openlmis.equipment.domain.EquipmentModel;
import org.openlmis.equipment.domain.EquipmentType;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO extends BaseModel implements Importable {

    @ImportField(type = "String", name = "Code")
    private String code;

    @ImportField(type = "String", name = "Name")
    private String name;

    @ImportField(type = "String", name = "Equipment Type Code", nested = "code")
    private EquipmentType equipmentType;

    @ImportField(type = "String", name = "Manufacturer Code", nested = "code")
    private Manufacturer facturer;

    private String manufacturer;

    private Long manufacturerId;

    private Long modelId;

    private String model;

    @ImportField(type = "String", name = "Equipment Model Code", nested = "code")
    private EquipmentModel equipmentModel;

    @ImportField(type = "String", name = "Energy Type Code", nested = "code")
    private EquipmentEnergyType energyType;

    @ImportField(type = "String", name = "Category", nested = "code")
    private EquipmentCategory equipmentCategory;

    private Long equipmentCategoryId;

}
