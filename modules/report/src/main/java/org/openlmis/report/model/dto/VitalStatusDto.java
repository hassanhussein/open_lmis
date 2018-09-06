package org.openlmis.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VitalStatusDto {
    private String model;
    private Long currentMonth;
    private Long lastThreeMonth;
    private Long lastSixMonth;
    private Long total;
}
