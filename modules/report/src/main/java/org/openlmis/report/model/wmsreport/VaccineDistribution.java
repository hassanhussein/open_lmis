package org.openlmis.report.model.wmsreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
public class VaccineDistribution {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date distributiondate;
    private String orderdate;
    private  int orderid,tofacilityid,periodid;
    private String facilityName,facility_type_from,facility_type_to,from_zone_name,
    to_zone_name,to_description,from_description,from_facility_name;

    public int getId() {
        return id;
    }

    public Date getDistributiondate() {
        return distributiondate;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public int getPeriodid() {
        return periodid;
    }

    public int getOrderid() {
        return orderid;
    }

    public int getTofacilityid() {
        return tofacilityid;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getFacility_type_from() {
        return facility_type_from;
    }

    public String getFacility_type_to() {
        return facility_type_to;
    }

    public String getFrom_zone_name() {
        return from_zone_name;
    }

    public String getTo_zone_name() {
        return to_zone_name;
    }

    public String getTo_description() {
        return to_description;
    }

    public String getFrom_description() {
        return from_description;
    }

    public String getFrom_facility_name() {
        return from_facility_name;
    }
}
