package org.openlmis.core.dto;

import lombok.Data;

@Data
public class NotificationResponseDTO {

    private String invoiceNumber;
    private String message;
    private String source;
}
