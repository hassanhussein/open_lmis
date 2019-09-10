package org.openlmis.core.dto;

import lombok.Data;

import javax.persistence.*;

@Data
public class DocumentDTO {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String docName;

    @Column
    @Lob
    private byte[] file;


}
