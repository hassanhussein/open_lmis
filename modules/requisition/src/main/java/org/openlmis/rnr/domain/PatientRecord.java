package org.openlmis.rnr.domain;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

@Data
public class PatientRecord extends BaseModel {

    private Long rnrId;
    private Integer newConfirmedCase;
    private Integer totalRecovered;
    private Integer totalDeath;
    private Integer transfer;
    private Integer numberOfCumulativeCases;
    private Integer patientOnTreatment;

}
