package org.openlmis.core.dto;

import lombok.Data;

@Data
public class ResponseDTO {

    String responseType;
    String status;
    String description;
    Long imported;
    Long updated;
    Long ignored;
    Long deleted;
    String affectedObject;
    String affectedValue;
    Boolean dataSetComplete;
}
