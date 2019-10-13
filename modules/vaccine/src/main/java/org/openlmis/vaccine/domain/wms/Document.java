package org.openlmis.vaccine.domain.wms;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class Document extends BaseModel {

    private DocumentType documentType;

    private Asn asn;

    private String fileLocation;

    private String asnNumber;

}
