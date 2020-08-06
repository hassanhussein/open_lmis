package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;

@Data
public class TransferDTO {

    private Long lotId;

    private Long lotOnHandId;

    private Long productId;

    private String productName;

    private String lotNumber;

    private Long quantityOnHand;

    private Long stockCardId;
    private String vvmId;
    private String vvm;
    private String lotVvm;
    private String lotNumberUn;

}
