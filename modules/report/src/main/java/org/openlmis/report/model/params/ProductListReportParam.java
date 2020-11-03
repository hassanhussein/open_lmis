package org.openlmis.report.model.params;

import lombok.Data;
import org.openlmis.report.model.ReportParameter;

@Data
public class ProductListReportParam extends BaseParam implements ReportParameter {

    private Long program;

    private Long facilityType;

    private Long productCategory;

}
