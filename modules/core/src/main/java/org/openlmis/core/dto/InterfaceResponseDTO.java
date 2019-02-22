package org.openlmis.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

import java.util.List;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class InterfaceResponseDTO extends BaseModel {

    String responseType;
    String status;
    String description;
    ImportCountDTO importCount;
    List<ConflictDTO> conflicts;
    Boolean dataSetComplete;

    String sourceOrderId;
    String code;
    Long rnrId;

    BudgetDTO budgetDTO;

}
