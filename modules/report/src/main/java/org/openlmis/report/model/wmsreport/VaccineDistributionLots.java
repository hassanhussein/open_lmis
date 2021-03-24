package org.openlmis.report.model.wmsreport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
public class VaccineDistributionLots {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private  int id;
    int lotId,quantity=0, distributionLineItemId,vialsNumber=0;
    String received=" ";
    String lotNumber,vvmStatus,binLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp expirationDate;

}
