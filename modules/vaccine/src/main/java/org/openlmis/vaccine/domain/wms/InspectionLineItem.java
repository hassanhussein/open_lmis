package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Product;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionLineItem extends BaseModel {

    private Long inspectionId;

    private Inspection inspection;

    private Long productId;

    private Product product;

    private Integer quantityCounted;

    private Integer boxCounted;

    private Integer passQuantity;

    private Integer passLocationId;

    private Integer failQuantity;

    private Integer failReason;

    private Integer failLocationId;

    private boolean lotFlag;

    private boolean dryIceFlag;

    private boolean icePackFlag;

    private boolean vvmFlag;

    private boolean ccCardFlag;

    private boolean electronicDeviceFlag;

    private boolean noCoolantFlag;

    private String otherMonitor;

    private List<InspectionLot> lots;


}
