package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;

@Data
public class SohReportDTO {

    private Long stockCardId;

    private Long productId;

    private String product;

    private String lotNumber;

    private String binLocation;

    private String lastUpdated;

    private Integer totalQuantityOnHand;

    private String expirationDate;

    private Integer quantityOnHand;

    private String vvm;

}
