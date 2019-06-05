package org.openlmis.report.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ResultRow;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFillRateReportInfo implements ResultRow {
    Integer totalcount;
    Long approved;
    Long shipped;
    Float   itemfillrate;
}
