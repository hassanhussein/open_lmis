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
public class EmergencyRnrAggregateByGeozoneReport implements ResultRow {

    String geograhicZone;
    String programArea;
    String period;
    Integer reported;

}
