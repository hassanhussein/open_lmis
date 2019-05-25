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
public class AggregateStockStatusReport implements ResultRow {

    private Integer soh;
    private Float mos;
    private Integer amc;
    private String status;
    private String code;
    private String product;
    private String facility;
    private String facilityType;
    private String color;
    private String periodName;
    private String district;
    private String requisitionStatus;
    private Long facilityId;
    private Integer minMonthsOfStock;
    private Integer maxMonthsOfStock;


}
