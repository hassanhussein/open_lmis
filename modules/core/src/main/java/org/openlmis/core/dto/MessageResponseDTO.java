package org.openlmis.core.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponseDTO {

    ELMISResponseMessageDTO message;
    String sourceOrderId;
    String rnrId;
}
