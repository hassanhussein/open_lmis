package org.openlmis.report.model.wmsreport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class StockCards {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    private int facilityId;
    private int totalQuantityOnHand;
    private  int productId;
    private String fullName;
    private String warehouseName,locationName,lotNumber;
    private Timestamp effectiveDate,expirationDate;
    private Timestamp modifiedDate;

    public int getId() {
        return id;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public int getTotalQuantityOnHand() {
        return totalQuantityOnHand;
    }

    public int getProductId() {
        return productId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public String getLocationName() {
        return locationName;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public String getLotNumber() {
        return lotNumber;
    }
}
