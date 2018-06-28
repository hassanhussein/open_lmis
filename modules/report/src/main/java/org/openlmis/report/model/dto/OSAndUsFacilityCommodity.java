package org.openlmis.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OSAndUsFacilityCommodity {
    private String facility;
    private String product;
    private String productCode;
    private Long mos;
    private String status;
    private Long stockInHand;
    private String rowNu;
}
