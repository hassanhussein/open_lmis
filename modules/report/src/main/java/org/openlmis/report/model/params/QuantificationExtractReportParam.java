package org.openlmis.report.model.params;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ReportParameter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuantificationExtractReportParam extends BaseParam implements ReportParameter {

   // @RequiredParam
    private Long program;
    private Long product;
    private Long period;
    private Long zone;
    private Long schedule;
    private String periodStart;
    private String periodEnd;

 public void setPeriodStart(String periodStart) {
  this.periodStart = periodStart;
 }
}
