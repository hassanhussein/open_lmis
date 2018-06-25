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
public class MonthlyStockStatusReport implements ResultRow {

    private String code;
    private String category;
    private String product;
    private String facilityCode;
    private String facility;
    private String facilityType;
    private String supplyingFacility;
    private int openingBalance;
    private int receipts;
    private int issues;
    private int adjustments;
    private int closingBalance;
    private Double monthsOfStock;
    private Double averageMonthlyConsumption;
    private Double maximumStock;
    private int reorderAmount;
    private Integer minMOS;
    private Integer maxMOS;
}
