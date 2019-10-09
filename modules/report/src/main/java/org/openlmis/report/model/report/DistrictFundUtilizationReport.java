package org.openlmis.report.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ResultRow;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistrictFundUtilizationReport implements ResultRow {

    private String region;
    private String facility;
    private String facilityType;
    private String zone_name;
    private String district;
    private Integer other;
    private Integer userFees;

}
