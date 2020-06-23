package org.openlmis.report.model.wmsreport;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StockCard {

    @Id
    int productId;
    String productName,wareHouseName;
    int totalQuantityOnHand;
    public int getTotalQuantityOnHand() {
        return totalQuantityOnHand;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getWareHouseName() {
        return wareHouseName;
    }
}
