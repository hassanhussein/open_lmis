package org.openlmis.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductExpiry {

    private Long programId;
    private Long periodId;
    private  String productCode;
    private Long balance;
    private  Long expired;
    private  Double percentage;
    private String product;
    private String facility;
    private String facilityid;
    private String period;
}
