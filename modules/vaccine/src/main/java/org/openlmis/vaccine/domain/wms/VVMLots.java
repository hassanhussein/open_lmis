package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.vaccine.dto.LocationDTO;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VVMLots extends BaseModel {
    private Long vvmId;
    private Long failVvmId;

    private Long quantity;

    private InspectionLineItem lineItem;

    private String lotNumber;

    private Integer countedQuantity;

    private Integer passQuantity;

    private Long passLocationId;

    private Long failQuantity;
    private Long failedQuantity;
    private Long failedReason;
    private Long failReason;

    private Long failLocationId;

    private Long vvmStatus;

    private Long inspectionLineItemId;

    private Date expiryDate;

    private Integer receivedQuantity;

    private List<InspectionLotProblem> problems;

    private String boxNumber;

    private LocationDTO location;
    private  InspectionFailProblem failed;

}
