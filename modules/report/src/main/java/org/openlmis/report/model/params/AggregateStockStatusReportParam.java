package org.openlmis.report.model.params;

import lombok.*;
import org.openlmis.report.annotations.RequiredParam;
import org.openlmis.report.model.ReportParameter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AggregateStockStatusReportParam extends BaseParam implements ReportParameter {

    @RequiredParam
    private Long program;
    private Long product;
    private Long period;
    private Long zone;
    private Long schedule;

    @RequiredParam
    private String periodStart;
    @RequiredParam
    private String periodEnd;
    private Boolean disaggregated;

}
