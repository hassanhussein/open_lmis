package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

import java.util.Date;
import java.util.List;


@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionLot extends BaseModel {

    private InspectionLineItem lineItem;

    private String lotNumber;

    private Integer countedQuantity;

    private Integer passQuantity;

    private Long passLocationId;

    private Integer failQuantity;

    private Long failReason;

    private Long failLocationId;

    private Location location;

    private Long vvmStatus;

    private Long inspectionLineItemId;

    private Date expiryDate;

    private Integer receivedQuantity;

    private List<InspectionLotProblem>problems;

}
