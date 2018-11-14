package org.openlmis.core.message;

import lombok.Data;
import org.openlmis.core.dto.MessageResponseDTO;

@Data
public class CustomMessage {
    String message;
    public CustomMessage(String data) {
       this.message = data;
    }

    public String toString() {
        return message;
    }

}
