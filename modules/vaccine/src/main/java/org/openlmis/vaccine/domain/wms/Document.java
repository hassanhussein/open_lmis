package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.serializer.DateDeserializer;

import java.util.Date;

@Data
public class Document extends BaseModel {

    private DocumentType documentType;

    private Asn asn;

    private String fileLocation;

    private String asnNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createddate;

    private  String createdByName;
    private  String deletionLocation;
    private String comment;
    private Long deletedBy;
    private Boolean deleted;

}
