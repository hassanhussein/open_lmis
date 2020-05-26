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
    private int facilityid;
    private int totalquantityonhand;
    private  int productid;
    private String fullname;
    private String facility_name;
    private Timestamp effectivedate;
    private Timestamp modifieddate;

    public int getId() {
        return id;
    }

    public int getFacilityid() {
        return facilityid;
    }

    public int getTotalquantityonhand() {
        return totalquantityonhand;
    }

    public int getProductid() {
        return productid;
    }

    public String getFullname() {
        return fullname;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public Timestamp getEffectivedate() {
        return effectivedate;
    }

    public Timestamp getModifieddate() {
        return modifieddate;
    }
}
