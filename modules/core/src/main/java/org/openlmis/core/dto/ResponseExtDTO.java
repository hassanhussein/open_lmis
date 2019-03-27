package org.openlmis.core.dto;

import lombok.Data;

@Data
public class ResponseExtDTO {

    String status;
    String description;
    String code;
    String sourceOrderId;

}
