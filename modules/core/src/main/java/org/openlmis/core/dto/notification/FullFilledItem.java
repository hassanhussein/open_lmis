package org.openlmis.core.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
public class FullFilledItem extends NotificationLineItem {

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

    public List<VisibleColumn> getColumns() {

        List<VisibleColumn>  visibleColumns = new ArrayList<>();

        visibleColumns.add(new VisibleColumn("itemCode","Item Code", true,40));
        visibleColumns.add(new VisibleColumn("itemDescription","Item Description", true,40));
        visibleColumns.add(new VisibleColumn("uom","uom", true,40));
        visibleColumns.add(new VisibleColumn("quantity","Quantity", true,40));
        visibleColumns.add(new VisibleColumn("batchSerialNo","Batch Serial No", true,40));
        visibleColumns.add(new VisibleColumn("batchQuantity","Batch Quantity", true,40));
        visibleColumns.add(new VisibleColumn("expiryDate","Expiry Date", true,40));
        visibleColumns.add(new VisibleColumn("unitPrice","Unit Price", true,40));
        visibleColumns.add(new VisibleColumn("amount","Amount", true,40));
        return visibleColumns;
    }

    @Override
    public String getValue(String columnName) throws NoSuchFieldException, IllegalAccessException {
        return null;
    }

    public Object getValueFor(String fieldName) {
        Object value = null;
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            value = field.get(this);
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        return value;
    }
}
