package org.openlmis.report.model.wmsreport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineDistributionLineItem extends BaseModel {
    //int id;
    private String facilityName,product,district,region,productCode,lotNumber;
    int quantityIssued, distributionId;
    int unitPrice;
    int amount;

    private String  uom="Doses";

    private void setAmount(){
        amount=unitPrice*quantityIssued;
    }

}
