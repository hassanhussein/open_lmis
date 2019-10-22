package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class CurrencyDTO extends BaseModel {

    String name;
    String code;

}
