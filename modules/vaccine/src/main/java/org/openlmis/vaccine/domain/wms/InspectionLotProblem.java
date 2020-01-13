package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionLotProblem extends BaseModel {

    private Long inspectionLotId;

    private InspectionLot inspectionLot;

    private Integer boxNumber;

    private String lotNumber;

    private boolean isalarma;

    private boolean isalarmb;

    private boolean isalarmc;

    private boolean isalarmd;

    private boolean iscca;

    private boolean isccb;

    private boolean isccc;

    private boolean isccd;

}
