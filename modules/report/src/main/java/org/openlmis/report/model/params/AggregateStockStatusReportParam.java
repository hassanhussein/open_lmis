package org.openlmis.report.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.report.annotations.RequiredParam;
import org.openlmis.report.model.ReportParameter;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
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

}
