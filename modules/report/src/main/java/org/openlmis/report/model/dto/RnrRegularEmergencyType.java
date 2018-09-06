package org.openlmis.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RnrRegularEmergencyType {
    private String periodId;
    private String period;
    private Long regular;
    private Long emergency;
    private Long total;
    private Double percent;
}
