package org.openlmis.equipment.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Program;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.domain.EquipmentModel;
import org.openlmis.equipment.domain.EquipmentOperationalStatus;
import org.openlmis.upload.annotation.ImportField;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonSerialize(include = NON_EMPTY)
public class EquipmentInventoryUploadDto extends EquipmentInventory {

    @ImportField(mandatory = true, name = "Program Code", nested = "code")
    private Program program;

    @ImportField(mandatory = true, name = "Equipment Model", nested = "code")
    private EquipmentModel equipmentModel;

    @ImportField(mandatory = true, name = "Equipment Operational Status", nested = "code")
    private EquipmentOperationalStatus  equipmentOperationalStatus;
}
