package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;

@Data
public class LotOnHandDTO {

    private Long lotId;

    private String lotNumber;

    private Long amount;

    private String vvm;

    private String expiry;

    private Integer id;

}
