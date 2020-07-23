package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class InterfaceLogDTO extends BaseModel {

    private String details;

    private Boolean isSent;

    private String statusCode;

    private String fileName;


}
