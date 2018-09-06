package org.openlmis.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentInterface {

    private  String orderDate;
    private Long packed;
    private Long released;
    private Long failed;
}
