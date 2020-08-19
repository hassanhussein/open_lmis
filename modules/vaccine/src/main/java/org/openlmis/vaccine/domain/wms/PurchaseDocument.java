package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.serializer.DateDeserializer;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseDocument extends BaseModel {

    Asn asn;

    DocumentType documentType;

    String fileLocation;

    Receive receive;

    private String asnNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createddate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")

    private Date docCreatedDate;

    private  String createdByName;

    private Boolean deleted;
    private  Long deletedBy;

    String comment;
    String deletionLocation;
    String deletedByName;


}
