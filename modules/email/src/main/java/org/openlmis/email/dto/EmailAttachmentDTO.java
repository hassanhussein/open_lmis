package org.openlmis.email.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EmailAttachmentDTO {

    private Long id;
    private String attachmentPath;
    private String attachmentName;
    private String attachmentFileType;
    private String fileDataSource;
    protected Date createdDate;
    private byte[] fileSource;
}
