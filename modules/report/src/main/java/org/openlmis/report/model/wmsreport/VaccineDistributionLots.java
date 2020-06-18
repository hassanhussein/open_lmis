package org.openlmis.report.model.wmsreport;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class VaccineDistributionLots {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    int lotId,quantity, distributionLineItemId;
    String lotNumber,vvmStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp expirationDate;

    public int getId() {
        return id;
    }

    public int getDistributionLineItemId() {
        return distributionLineItemId;
    }

    public int getLotId() {
        return lotId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getVvmStatus() {
        return vvmStatus;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }
}
