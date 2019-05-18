package org.openlmis.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ResultRow;
import org.openlmis.rnr.domain.SourceOfFunds;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundSourceReport implements ResultRow {

    List<SourceOfFunds> sourceOfFundsList;
    String name;
    Integer quantity;
}
