package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

import java.util.List;

@Data
public class InterfaceResponseDTO extends BaseModel {

    String responseType;
    String status;
    String description;
    ImportCountDTO importCount;
    List<ConflictDTO> conflicts;
    Boolean dataSetComplete;


}
