package org.openlmis.core.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.openlmis.core.domain.BaseModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "itemCode",
        "itemDescription",
        "uom",
        "quantity",
        "missingItemStatus"
})
public class StockOutItem  extends NotificationLineItem {

    private Long notificationId;

    @JsonProperty("itemCode")
    public String itemCode;
    @JsonProperty("itemDescription")
    public String itemDescription;
    @JsonProperty("uom")
    public String uom;
    @JsonProperty("quantity")
    public String quantity;
    @JsonProperty("missingItemStatus")
    public String missingItemStatus;




    public List<VisibleColumn> getColumns() {

        List<VisibleColumn> visibleColumns = new ArrayList<>();
        visibleColumns.add(new VisibleColumn("itemCode","Item Code", true,40));
        visibleColumns.add(new VisibleColumn("itemDescription","Item Description", true,40));
        visibleColumns.add(new VisibleColumn("uom","uom", true,40));
        visibleColumns.add(new VisibleColumn("quantity","Quantity", true,40));
        visibleColumns.add(new VisibleColumn("missingItemStatus","Missing Status", true,40));
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
