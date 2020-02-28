package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;

@Data
public class PutAwayDTO {

    private Long id;

    private String binLocation;

    private String warehouseName;

    private String inspectionDate;

    private String poNumber;


}
