package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LotOnHandExtDTO {

    private String product;

    private Long quantityOnHand;

    private Long lotId;

    private String lotNumber;

    private Date expiry;

    private Long maxSoh;

    private String packSize;

    private String vvmStatus;

}
