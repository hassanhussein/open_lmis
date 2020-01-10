package org.openlmis.report.model.params;

import lombok.*;
import org.openlmis.report.model.ReportParameter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemFillRateReportParam extends BaseParam implements ReportParameter {
    private Long period;

    private Long program;

    private Long zone;

    private String product;

    private Long productCategory;

    private Long facility;

    public Long orderId;

}
