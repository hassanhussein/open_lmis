package org.openlmis.core.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;

@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "itemCode",
        "itemDescription",
        "uom",
        "quantity",
        "batchSerialNo",
        "batchQuantity",
        "expiryDate",
        "unitPrice",
        "amount"
})
public class FullFilledItem extends BaseModel {

    private Long notificationId;

    @JsonProperty("itemCode")
    public String itemCode;
    @JsonProperty("itemDescription")
    public String itemDescription;
    @JsonProperty("uom")
    public String uom;
    @JsonProperty("quantity")
    public String quantity;
    @JsonProperty("batchSerialNo")
    public String batchSerialNo;
    @JsonProperty("batchQuantity")
    public String batchQuantity;
    @JsonProperty("expiryDate")
    public String expiryDate;
    @JsonProperty("unitPrice")
    public String unitPrice;
    @JsonProperty("amount")
    public String amount;

}
