package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

@Data
public class ProductDTO extends BaseModel implements Importable {

    @ImportField(name = "Owner/Revision", mandatory = true)
    private String revision;

    @ImportField(name ="Old Product Code", mandatory = true)
    private String productCode;

    @ImportField(name = "New Product Code", mandatory = true)
    private String newProductCode;


}
