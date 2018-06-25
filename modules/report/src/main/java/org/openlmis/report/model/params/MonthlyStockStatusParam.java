package org.openlmis.report.model.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.annotations.RequiredParam;
import org.openlmis.report.model.ReportParameter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStockStatusParam extends BaseParam implements ReportParameter {

    @RequiredParam
    private Long program;

    @RequiredParam
    private Long schedule;

    @RequiredParam
    private Long period;

    private String endDate;

    private String startDate;

    private Long zone;

    private Long facilityType;

    private Boolean disaggregated;

    private Long productCategory;

    private Long product;

    private Long facility;

    private Boolean isEmergency;

    private String reportType;

    private Boolean allReportType;
}
