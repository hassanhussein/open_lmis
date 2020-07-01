package org.openlmis.stockmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Product;
import org.openlmis.core.serializer.DateDeserializer;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

import java.util.Date;

/**
 * Lot represents a product-batch, with a specific manufacturer, manufacture date, etc.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Lot extends BaseModel implements Importable
{
    @ImportField(mandatory = true, name = "Product Code", nested = "code")
    private Product product;

    @ImportField(mandatory = true, name = "Lot Code")
    private String lotCode;

    @ImportField(mandatory = true, name = "Manufacturer Name")
    private String manufacturerName;

    @ImportField(mandatory = true, type = "Date", name = "Manufacture Date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonDeserialize(using=DateDeserializer.class)
    private Date manufactureDate;

    @ImportField(type = "Date", name = "Expiration Date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonDeserialize(using=DateDeserializer.class)
    private Date expirationDate;

    public final boolean isValid() {
        return (null != lotCode && !lotCode.equals("") &&
            null != manufacturerName && !manufacturerName.equals("") &&
            null != expirationDate);
    }
    private Long productId;

    private String lotNumber;

    private String packSize;

    private Long vvmId;

}
