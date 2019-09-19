package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openlmis.core.domain.BaseModel;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseDocument extends BaseModel {

    Asn asn;

    DocumentType documentType;

    String fileLocation;

    Receive receive;
}
