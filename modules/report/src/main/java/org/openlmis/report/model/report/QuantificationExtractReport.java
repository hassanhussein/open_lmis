package org.openlmis.report.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.Pagination;
import org.openlmis.report.model.ResultRow;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuantificationExtractReport implements ResultRow {

    private String facilityCode;
    private String facility;
    private String facilityType;
    private String code;
    private String product;
    private String unit;
    private Integer uom;
    private double issues;
    private Float consumption;
    private int totalRecords;
    private Pagination pagination;
    private String periodName;
    private String district;

}
