package org.openlmis.report.model.wmsreport;

import lombok.Data;

import javax.persistence.*;

@Data
public class VaccineDistributionLineItem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    private String facilityName,product,district,region,productCode;
    int quantityIssued, distributionId;

    private String  uom="Doses";

}
