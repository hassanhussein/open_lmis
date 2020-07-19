package org.openlmis.report.model.wmsreport;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class VaccineDistribution {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date distributionDate;
    private String orderDate;
    private  long pickListId;
    private  int orderId, toFacilityId, periodId;
    private String facilityName, facilityTypeFrom, facilityTypeTo, fromZoneName,
            toZoneName, toDescription, fromDescription, fromFacilityName;

    public int getId() {
        return id;
    }

    public Date getDistributionDate() {
        return distributionDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getPeriodId() {
        return periodId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getToFacilityId() {
        return toFacilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public long getPickListId() {
        return pickListId;
    }

    public String getFacilityTypeFrom() {
        return facilityTypeFrom;
    }

    public String getFacilityTypeTo() {
        return facilityTypeTo;
    }

    public String getFromZoneName() {
        return fromZoneName;
    }

    public String getToZoneName() {
        return toZoneName;
    }

    public String getToDescription() {
        return toDescription;
    }

    public String getFromDescription() {
        return fromDescription;
    }

    public String getFromFacilityName() {
        return fromFacilityName;
    }
}
