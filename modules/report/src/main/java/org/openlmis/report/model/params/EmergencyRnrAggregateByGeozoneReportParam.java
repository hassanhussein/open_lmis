package org.openlmis.report.model.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.annotations.RequiredParam;
import org.openlmis.report.model.ReportParameter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyRnrAggregateByGeozoneReportParam extends BaseParam
        implements ReportParameter {

    private String action;

    @RequiredParam
    private String periodStart;

    @RequiredParam
    private String periodEnd;

    @RequiredParam
    private Long program;
    private Long zone;
}
