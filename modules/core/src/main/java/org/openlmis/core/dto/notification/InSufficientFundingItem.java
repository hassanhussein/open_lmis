package org.openlmis.core.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InSufficientFundingItem extends NotificationLineItem {

    private Long notificationId;
    @JsonProperty("itemCode")
    public String itemCode;
    @JsonProperty("itemDescription")
    public String itemDescription;
    @JsonProperty("uom")
    public String uom;
    @JsonProperty("quantity")
    public String quantity;
    @JsonProperty("quantityShipped")
    public String quantityShipped;
    @JsonProperty("quantityOrdered")
    public String quantityOrdered;
    @JsonProperty("missingItemStatus")
    public String missingItemStatus;
    @JsonProperty("dueDate")
    public Date dueDate;

    public List<VisibleColumn> getColumns() {

        List<VisibleColumn> visibleColumns = new ArrayList<>();
        visibleColumns.add(new VisibleColumn("itemCode","Item Code", true,40));
        visibleColumns.add(new VisibleColumn("itemDescription","Item Description", true,40));
        visibleColumns.add(new VisibleColumn("uom","uom", true,40));
        visibleColumns.add(new VisibleColumn("quantity","Quantity", true,40));
        visibleColumns.add(new VisibleColumn("quantityShipped","Quantity Shipped", true,40));
        visibleColumns.add(new VisibleColumn("quantityOrdered","Quantity Ordered", true,40));
        visibleColumns.add(new VisibleColumn("missingItemStatus","Missing Status", true,40));
        visibleColumns.add(new VisibleColumn("dueDate","Due Date", true,40));
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
