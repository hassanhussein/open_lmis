package org.openlmis.restapi.domain;

import lombok.Data;

@Data
public class MessageDTO {
    String status;
    String code;
    String description;
    String productCode;

}
