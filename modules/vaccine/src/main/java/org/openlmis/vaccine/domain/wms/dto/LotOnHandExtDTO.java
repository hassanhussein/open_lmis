package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;

@Data
public class LotOnHandExtDTO {

    private String product;

    private Long quantityOnHand;

    private Long lotId;

    private String lotNumber;

    private String expiry;

    private Long maxSOH;

}
