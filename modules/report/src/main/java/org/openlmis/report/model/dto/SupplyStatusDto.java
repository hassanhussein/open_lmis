package org.openlmis.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplyStatusDto {
    private  String productCode;
    private String product;
    private  Long fillZero;
    private Long lessThan25;
    private Long lessThan50;
    private Long lessThan75;
}
