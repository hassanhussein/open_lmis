package org.openlmis.rnr.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class ReasonDTO extends BaseModel {

    String name;
    String code;
    Boolean selected;
}
