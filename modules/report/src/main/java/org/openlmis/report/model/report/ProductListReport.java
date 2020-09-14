package org.openlmis.report.model.report;

import lombok.Data;
import org.openlmis.report.model.ResultRow;

@Data
public class ProductListReport  implements ResultRow {

    private String facilityType;

    private String programName;

    private String productCode;

    private String productName;

    private Double maxMonthsOfStock;

    private Double minMonthsOfStock;

    private Double eop;

    private Boolean isActive;

}
