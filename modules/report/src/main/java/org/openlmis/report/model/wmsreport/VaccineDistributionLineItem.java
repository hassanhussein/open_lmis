package org.openlmis.report.model.wmsreport;

import javax.persistence.*;

@Entity
public class VaccineDistributionLineItem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    private String facilityName,product,district,region;
    int quantityIssued, distributionId;



    public int getId() {
        return id;
    }

    public int getDistributionId() {
        return distributionId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public String getProduct() {
        return product;
    }

    public String getDistrict() {
        return district;
    }

    public String getRegion() {
        return region;
    }

    public int getQuantityIssued() {
        return quantityIssued;
    }
}
