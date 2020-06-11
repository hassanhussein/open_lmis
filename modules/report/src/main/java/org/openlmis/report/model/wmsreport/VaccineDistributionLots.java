package org.openlmis.report.model.wmsreport;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;
@Entity
public class VaccineDistributionLots {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    int lotid,quantity,vvmstatus,distributionlineitemid;
    String lotnumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp expirationdate;

    public int getId() {
        return id;
    }

    public int getDistributionlineitemid() {
        return distributionlineitemid;
    }

    public int getLotid() {
        return lotid;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getVvmstatus() {
        return vvmstatus;
    }

    public String getLotnumber() {
        return lotnumber;
    }

    public Timestamp getExpirationdate() {
        return expirationdate;
    }
}
