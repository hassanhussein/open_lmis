package org.openlmis.report.model.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ReportParameter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistrictFundUtilizationParam extends BaseParam implements ReportParameter {


    private Long period;

    private Long program;

    private Long zone;

    private String product;

    private Long productCategory;

    private Long facility;
}
