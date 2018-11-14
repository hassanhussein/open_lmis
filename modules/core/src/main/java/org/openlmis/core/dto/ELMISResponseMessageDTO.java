package org.openlmis.core.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ELMISResponseMessageDTO{

        String code;
        String description;
        String usage;
        Integer displayOrder;


}
