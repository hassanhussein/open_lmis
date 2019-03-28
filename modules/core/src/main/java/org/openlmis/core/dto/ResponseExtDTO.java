package org.openlmis.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseExtDTO {

    String status;
    String description;
    String code;
    @JsonProperty("sourceorderid")
    String sourceOrderId;

}
